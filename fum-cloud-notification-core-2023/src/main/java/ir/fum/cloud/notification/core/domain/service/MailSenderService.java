package ir.fum.cloud.notification.core.domain.service;


import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailConfigRepository;
import ir.fum.cloud.notification.core.domain.model.helper.GenericResponse;
import ir.fum.cloud.notification.core.domain.model.request.SendMailRequest;
import ir.fum.cloud.notification.core.domain.model.srv.SendMailSrv;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import ir.fum.cloud.notification.core.util.request.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailSenderService {


    private final SessionFactory sessionFactory;
    private final MailConfigRepository mailConfigRepository;

    private final MailProcessorFactory mailProcessorFactory;


    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public GenericResponse<SendMailSrv> sendMail(UserVO user,
                                                 SendMailRequest sendMailRequest,
                                                 boolean isBulk) throws NotificationException {
        Session session = sessionFactory.getCurrentSession();

        MailConfig mailConfig = getAndCheckMailConfig(session, sendMailRequest.getServiceName(), user.getUser_id());

        MailProcessor mailProcessor = mailProcessorFactory.getMailProcessor(isBulk);

        String requestId = getRequestId();

        SendMailSrv response = mailProcessor.processMailRequest(mailConfig, sendMailRequest, requestId, user);

        return ResponseUtil.getResponse(response);
    }

    private String getRequestId() {
        return UUID.randomUUID().toString();
    }

    private MailConfig getAndCheckMailConfig(Session session, String serviceName, long ownerId) throws NotificationException {
        //todo: read from cache ?
        return mailConfigRepository.getMailConfigByOwnerAndServiceName(session, serviceName, ownerId)
                .orElseThrow(() -> NotificationException.exception(
                        NotificationExceptionStatus.NOT_FOUND,
                        "تنظیمات وارد شده موجود نمی باشد."
                ));

    }

}
