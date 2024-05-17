package ir.fum.cloud.notification.core.domain.service;


import com.fasterxml.jackson.core.type.TypeReference;
import ir.fum.cloud.notification.core.configuration.MailResendPropertiesConfiguration;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailItem;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailRequest;
import ir.fum.cloud.notification.core.data.hibernate.entity.model.RequestState;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailConfigRepository;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailItemRepository;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailRequestRepository;
import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.domain.model.request.SendMailRequest;
import ir.fum.cloud.notification.core.domain.model.vo.MailQueueVO;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailRequestMetadata;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailVO;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.queue.MailResendProducer;
import ir.fum.cloud.notification.core.util.GeneralUtils;
import ir.fum.cloud.notification.core.util.mapper.JsonUtils;
import ir.fum.cloud.notification.core.util.request.ResponseUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class MailResendService extends MailQueueConnector {


    private final MailLimitationService mailLimitationService;
    private final MailResendProducer mailResendProducer;
    private final MailResendPropertiesConfiguration mailResendPropertiesConfiguration;
    private final MailSender mailSender;

    private final SessionFactory sessionFactory;

    private final MailItemRepository mailItemRepository;

    public MailResendService(MailConfigRepository mailConfigRepository,
                             MailRequestRepository mailRequestRepository,
                             MailLimitationService mailLimitationService,
                             MailResendProducer mailResendProducer,
                             MailResendPropertiesConfiguration mailResendPropertiesConfiguration,
                             @Lazy MailSender mailSender,
                             SessionFactory sessionFactory, MailItemRepository mailItemRepository) {

        super(mailConfigRepository, mailRequestRepository);
        this.mailLimitationService = mailLimitationService;
        this.mailResendProducer = mailResendProducer;
        this.mailResendPropertiesConfiguration = mailResendPropertiesConfiguration;
        this.mailSender = mailSender;
        this.sessionFactory = sessionFactory;
        this.mailItemRepository = mailItemRepository;

    }

    private static SendMailVO getSendMailVOFromMailItem(MailItem mailItem, UserVO user, Map<String, Object> metadata, String messageId, SendMailRequest sendMailRequest) {
        SendMailRequestMetadata sendMailRequestMetadata = new SendMailRequestMetadata(mailItem.getReceiver(), messageId);
        sendMailRequestMetadata.setMetadata(metadata);

        SendMailVO sendMailVO = SendMailVO.builder()
                .requestId(mailItem.getMailRequest().getRequest().getNotificationId())
                .sendMailRequest(sendMailRequest)
                .mailConfig(mailItem.getMailRequest().getMailConfig())
                .resendCounter(-1)
                .resend(true)
                .throwException(false)
                .messageIds(Collections.singletonList(messageId))
                .requestInformation(Collections.singletonMap(messageId, sendMailRequestMetadata))
                .receivers(Collections.singletonList(mailItem.getReceiver()))
                .user(user.getUser_id())
                .build();
        return sendMailVO;
    }

    private static SendMailRequest getSendMailRequestFromMailItem(MailItem mailItem) {
        String[] splitedBody = mailItem.getMailRequest().getBody().split(MailProcessor.CONTENT_SEPARATOR);

        String content = GeneralUtils.isNullOrEmpty(splitedBody[0]) || splitedBody[0].equals("null") ? null : splitedBody[0];
        String plainText = GeneralUtils.isNullOrEmpty(splitedBody[1]) || splitedBody[1].equals("null") ? null : splitedBody[1];


        SendMailRequest sendMailRequest = SendMailRequest.builder()
                .bcc(mailItem.getMailRequest().getBcc())
                .cc(mailItem.getMailRequest().getCc())
                .serviceName(mailItem.getMailRequest().getMailConfig().getName())
                .resend(true)
                .content(content)
                .plainText(plainText)
                .replayAddress(mailItem.getMailRequest().getReplyAddress())
                .subject(mailItem.getMailRequest().getSubject())
                .to(Collections.singletonList(mailItem.getReceiver()))
                .build();
        return sendMailRequest;
    }

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public GenericResponse<String> resend(UserVO user) throws NotificationException {
        Session session = sessionFactory.getCurrentSession();

        List<MailItem> failedMailItems = mailItemRepository.getFailedMails(session, user);


        failedMailItems.forEach(mailItem -> generateMailInfoAndResend(mailItem, user));


        return ResponseUtil.getResponse("درخواست شما در صف پردازش قرار گرفته است.");
    }

    private void generateMailInfoAndResend(MailItem mailItem, UserVO user) {
        Map<String, Object> metadata = JsonUtils.getObject(
                mailItem.getMetaData(),
                new TypeReference<Map<String, Object>>() {
                }
        );

        String messageId = (String) metadata.getOrDefault("messageId", null);

        if (GeneralUtils.isNullOrEmpty(messageId)) {
            return;
        }

        SendMailRequest sendMailRequest = getSendMailRequestFromMailItem(mailItem);

        SendMailVO sendMailVO = getSendMailVOFromMailItem(mailItem, user, metadata, messageId, sendMailRequest);

        resend(sendMailVO);

    }

    public void resend(SendMailVO sendMailVO) {


        sendMailVO.setResend(true);
        sendMailVO.setResendCounter(sendMailVO.getResendCounter() + 1);
        sendMailVO.setThrowException(false);


        List<String> receivers = new ArrayList<>();

        sendMailVO.getRequestInformation()
                .forEach((messageId, info) -> {
                    if (info.getState() == RequestState.RESEND_QUEUE) {
                        info.setState(null);
                        receivers.add(info.getReceiver());
                    }
                });


        log.info(
                "resend requested with requestId {} and receivers {} ",
                sendMailVO.getRequestId(),
                String.join(", ", receivers)
        );

        long delay = sendMailVO.getResendCounter() == 0 ?
                0L :
                (long) (mailResendPropertiesConfiguration.getFirstRetryInterval() *
                        Math.pow(mailResendPropertiesConfiguration.getTryIncrementFactor(), sendMailVO.getResendCounter())) * 60000L;

        mailResendProducer.sendMessage(
                JsonUtils.getStringJson(generateMailQueueVO(sendMailVO)),
                delay
        );
    }


    public void handleResendRecord(MailQueueVO mailQueueVO) {

        log.info("resend request received {} ", mailQueueVO.getRequestId());
        Pair<SendMailVO, MailRequest> info = getMailInfoFromQueueVO(mailQueueVO);

        try {
            mailLimitationService.checkOneMinuteLimitation(info.getLeft());
            mailSender.sendMail(info.getLeft(), info.getRight());

        } catch (NotificationException e) {
            log.error(e);
        }

    }

}
