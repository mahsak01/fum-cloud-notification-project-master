package ir.fum.cloud.notification.core.domain.service;

import ir.fum.cloud.notification.core.configuration.MailPropertiesConfiguration;
import ir.fum.cloud.notification.core.data.hibernate.entity.MailLimitation;
import ir.fum.cloud.notification.core.data.hibernate.entity.model.RequestState;
import ir.fum.cloud.notification.core.data.hibernate.repository.MailLimitationRepository;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailRequestMetadata;
import ir.fum.cloud.notification.core.domain.model.vo.SendMailVO;
import ir.fum.cloud.notification.core.exception.NotificationException;
import ir.fum.cloud.notification.core.exception.NotificationExceptionStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.PessimisticLockException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailLimitationService {
    private final MailLimitationRepository mailLimitationRepository;
    private final MailPropertiesConfiguration mailPropertiesConfiguration;
    private final SessionFactory sessionFactory;

    private Pair<Boolean, String> checkAndApplyMailLimitation(Session session, String from, String to, boolean throwException) throws NotificationException {

        try {


            Optional<MailLimitation> mailLimitation = mailLimitationRepository.getMailLimitation(
                    session,
                    to,
                    from
            );

            if (mailLimitation.isPresent()) {
                boolean oneMinutePassed = mailLimitation.get()
                        .getLastUpdateTimeCounter()
                        .plusMinutes(1)
                        .isBefore(LocalDateTime.now());

                int threshold = oneMinutePassed ? 0 : mailLimitation.get().getCounter() + 1;

                if (threshold >= mailPropertiesConfiguration.getMailSendingPerMinuteLimit()) {
                    return Pair.of(true, "exceed limitation in one minute");

                } else {
                    MailLimitation entity = mailLimitation.get();
                    entity.setCounter(threshold);
                    entity.setLastUpdateTimeCounter(LocalDateTime.now());

                    updateMailLimitation(session, entity, 0, throwException);

                }

            } else {

                MailLimitation entity = MailLimitation.builder()
                        .sender(from)
                        .receiver(to)
                        .build();

                mailLimitationRepository.save(session, entity);

            }
        } catch (Exception e) {
            log.error(e);
        }
        return Pair.of(false, null);
    }

    private void updateMailLimitation(Session session,
                                      MailLimitation mailLimitation,
                                      int counter,
                                      boolean throwError) throws NotificationException {
        try {
            mailLimitationRepository.update(session, mailLimitation);

        } catch (PessimisticLockException e) {
            log.error("optimistic lock exception, waiting to retry ...");

            if (counter < mailPropertiesConfiguration.getMailSendingPerMinuteLimit()) {
                MailLimitation modifiedMailLimitation = mailLimitationRepository.getMailLimitationById(session, mailLimitation.getId()).get();

                mailLimitation.setCounter(modifiedMailLimitation.getCounter() + 1);

                updateMailLimitation(session, mailLimitation, counter + 1, throwError);

            } else {

                if (throwError) {
                    throw NotificationException.exception(
                            NotificationExceptionStatus.INTERNAL_ERROR,
                            "خطای دیتابیس : optimistic lock exception"
                    );
                }
            }

        }
    }


    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public void checkOneMinuteLimitation(SendMailVO sendMailVO) throws NotificationException {
        String from = sendMailVO.getMailConfig().getAddress();

        for (String messageId : sendMailVO.getMessageIds()) {
            SendMailRequestMetadata info = sendMailVO.getRequestInformation().get(messageId);

            String receiver = info.getReceiver();

            Session session = sessionFactory.getCurrentSession();

            Pair<Boolean, String> limitationResult = checkAndApplyMailLimitation(session, from, receiver, sendMailVO.isThrowException());

            if (limitationResult.getLeft()) {
                info.addToMetadata("error", limitationResult.getRight());
                info.setState(RequestState.EXCEED_LIMITATION);
                sendMailVO.getRequestInformation().put(messageId, info);
            }

        }
    }

}
