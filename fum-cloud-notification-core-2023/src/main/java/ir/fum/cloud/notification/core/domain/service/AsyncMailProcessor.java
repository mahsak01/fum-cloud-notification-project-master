package ir.fum.cloud.notification.core.domain.service;

import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailRequestRepository;
import ir.fum.cloud.notification.core.data.hibernate.repository.RequestRepository;
import ir.fum.cloud.notification.core.domain.model.request.SendMailRequest;
import ir.fum.cloud.notification.core.domain.model.srv.SendMailSrv;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailRequestMetadata;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailVO;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.util.GeneralUtils;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
@Log4j2
public class AsyncMailProcessor extends MailProcessor {

    private final BulkQueueConnector bulkQueueConnector;


    private final ExecutorService threadPool;

    public AsyncMailProcessor(SessionFactory sessionFactory,
                              BulkQueueConnector bulkQueueConnector,
                              @Qualifier("mail.bulk.thread.pool") ExecutorService threadPool,
                              RequestRepository requestRepository,
                              MailRequestRepository mailRequestRepository,
                              MailLimitationService mailLimitationService) {

        super(sessionFactory, mailLimitationService, requestRepository, mailRequestRepository);
        this.bulkQueueConnector = bulkQueueConnector;


        this.threadPool = threadPool;
    }

    @Override

    public SendMailSrv processMailRequest(MailConfig mailConfig,
                                          SendMailRequest sendMailRequest,
                                          String requestId,
                                          UserVO user) throws NotificationException {


        createMailRequest(sendMailRequest, requestId, user, mailConfig);

        threadPool.submit(mailProcessorTask(mailConfig, sendMailRequest, user, requestId, Collections.emptyList()));

        return SendMailSrv.builder()
                .requestId(requestId)
                .description("درخواست شما در صف پردازش قرار گرفته است.")
                .build();
    }

    private Runnable mailProcessorTask(MailConfig mailConfig,
                                       SendMailRequest sendMailRequest,
                                       UserVO user,
                                       String requestId,
                                       List<String> fileNames) {
        return () -> {
            for (Object obj : sendMailRequest.getTo()) {
                String receiver = String.valueOf(obj);

                threadPool.submit(sendMailTask(mailConfig, sendMailRequest, user, requestId, receiver, fileNames));


            }
        };
    }

    private Runnable sendMailTask(MailConfig mailConfig,
                                  SendMailRequest sendMailRequest,
                                  UserVO user,
                                  String requestId,
                                  String receiver,
                                  List<String> fileNames) {
        return () -> {
            String messageId = GeneralUtils.generateUniqueId();

            log.info("task for sending email to {} with messageId {} and requestId {}", receiver, messageId, requestId);

            SendMailVO sendMailVO = getSendMailVO(mailConfig, sendMailRequest, user, requestId, messageId, fileNames);


//            boolean hasError = !sendMailPreCondition.receiverIsValid(receiver, sendMailRequest.getReceiverType());
//
//            if (hasError) {
//                sendMailVO.getRequestInformation().get(messageId)
//                        .setState(RequestState.INVALID_DATA);
//
//                sendMailVO.getRequestInformation().get(messageId)
//                        .addToMetadata("error", "invalid receiver");
//
//                sendMailVO.getRequestInformation().get(messageId)
//                        .setReceiver(receiver);
//
//            }

            String emailAddress = receiver;


            sendMailVO.setReceivers(Collections.singletonList(emailAddress));
            sendMailVO.getRequestInformation().get(messageId)
                    .setReceiver(emailAddress);


            try {
                mailLimitationService.checkOneMinuteLimitation(sendMailVO);
            } catch (NotificationException e) {
                log.error(e);
            }

            bulkQueueConnector.sendToQueue(sendMailVO);

        };

    }

    private SendMailVO getSendMailVO(MailConfig mailConfig,
                                     SendMailRequest sendMailRequest,
                                     UserVO user,
                                     String requestId,
                                     String messageId,
                                     List<String> fileNames) {
        SendMailRequest tempSendMailRequest = getTemporarySendMailRequest(sendMailRequest);

        return SendMailVO.builder()
                .fileNames(fileNames)
                .mailConfig(mailConfig)
                .resend(false)
                .messageIds(Collections.singletonList(messageId))
                .requestInformation(Collections.singletonMap(messageId, new SendMailRequestMetadata(messageId)))
                .sendMailRequest(tempSendMailRequest)
                .user(user.getUser_id())
                .requestId(requestId)
                .throwException(false)
                .resendCounter(0)
                .build();
    }

    private SendMailRequest getTemporarySendMailRequest(SendMailRequest sendMailRequest) {
        return SendMailRequest.builder()
                .cc(sendMailRequest.getCc())
                .bcc(sendMailRequest.getBcc())
                .serviceName(sendMailRequest.getServiceName())
                .content(sendMailRequest.getContent())
                .plainText(sendMailRequest.getPlainText())
                .subject(sendMailRequest.getSubject())
                .fileHashes(sendMailRequest.getFileHashes())
                .resend(sendMailRequest.isResend())
                .replayAddress(sendMailRequest.getReplayAddress())
                .build();
    }


}
