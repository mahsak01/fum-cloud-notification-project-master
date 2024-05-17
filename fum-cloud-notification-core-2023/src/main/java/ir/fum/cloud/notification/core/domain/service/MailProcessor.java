package ir.fum.cloud.notification.core.domain.service;

import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailRequest;
import ir.fum.cloud.notification.core.data.hibernate.entity.RequestModel;
import ir.fum.cloud.notification.core.data.hibernate.entity.model.MessageType;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailRequestRepository;
import ir.fum.cloud.notification.core.data.hibernate.repository.RequestRepository;
import ir.fum.cloud.notification.core.domain.model.request.SendMailRequest;
import ir.fum.cloud.notification.core.domain.model.srv.SendMailSrv;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Log4j2
public abstract class MailProcessor {

    public static final String CONTENT_SEPARATOR = "<mySeparator>";
    protected final SessionFactory sessionFactory;
    protected final MailLimitationService mailLimitationService;
    private final RequestRepository requestRepository;
    private final MailRequestRepository mailRequestRepository;

    public abstract SendMailSrv processMailRequest(MailConfig mailConfig,
                                                   SendMailRequest sendMailRequest,
                                                   String requestId,
                                                   UserVO user) throws NotificationException;

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    protected MailRequest createMailRequest(SendMailRequest sendMailRequest,
                                            String requestId,
                                            UserVO user,
                                            MailConfig mailConfig) {
        Session session = sessionFactory.getCurrentSession();
        MailRequest mailRequest = createMailRequest(session, sendMailRequest, requestId, user, mailConfig);


        return mailRequest;
    }


    protected MailRequest createMailRequest(org.hibernate.Session session,
                                            SendMailRequest sendMailRequest,
                                            String requestId,
                                            UserVO user,
                                            MailConfig mailConfig) {

        RequestModel requestModel = RequestModel.builder()
                .messageType(MessageType.SEND_EMAIL_MESSAGE_TYPE.getType())
                .notificationId(requestId.split(":")[0])
                .userId(user.getUser_id())
                .build();

        requestRepository.save(session, requestModel);

        MailRequest mailRequest = MailRequest.builder()
                .request(requestModel)
                .mailConfig(mailConfig)
                .bcc(sendMailRequest.getBcc())
                .cc(sendMailRequest.getCc())
                .body(sendMailRequest.getContent() + CONTENT_SEPARATOR + sendMailRequest.getPlainText())
                .replyAddress(sendMailRequest.getReplayAddress())
                .subject(sendMailRequest.getSubject())
                .build();

        mailRequestRepository.save(session, mailRequest);

        return mailRequest;

    }


}
