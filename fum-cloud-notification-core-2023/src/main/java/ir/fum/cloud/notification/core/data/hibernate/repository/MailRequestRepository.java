package ir.fum.cloud.notification.core.data.hibernate.repository;

import ir.fum.cloud.notification.core.data.hibernate.entity.MailRequest;
import ir.fum.cloud.notification.core.data.hibernate.entity.model.MessageType;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MailRequestRepository extends BaseRepository<MailRequest> {
    private final SessionFactory sessionFactory;

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public MailRequest findByRequestId(String requestId) {

        return sessionFactory.getCurrentSession()
                .createQuery("select mailRequest " +
                                "from MailRequest mailRequest " +
                                "left join fetch mailRequest.request request " +
                                "where " +
                                "request.notificationId = :requestId " +
                                "and " +
                                "request.messageType = :messageType",
                        MailRequest.class)
                .setParameter("requestId", requestId)
                .setParameter("messageType", MessageType.SEND_EMAIL_MESSAGE_TYPE.getType())
                .uniqueResult();
    }
}
