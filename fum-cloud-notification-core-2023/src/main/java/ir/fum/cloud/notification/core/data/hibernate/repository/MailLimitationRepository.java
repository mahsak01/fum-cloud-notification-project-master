package ir.fum.cloud.notification.core.data.hibernate.repository;

import ir.fum.cloud.notification.core.data.hibernate.entity.MailLimitation;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MailLimitationRepository extends BaseRepository<MailLimitation> {
    public Optional<MailLimitation> getMailLimitation(Session session, String receiver, String sender) {

        return session.createQuery("select mLimit from MailLimitation mLimit " +
                                "where " +
                                "mLimit.receiver = :receiver " +
                                "and " +
                                "mLimit.sender = :sender ",
                        MailLimitation.class)
                .setParameter("receiver", receiver)
                .setParameter("sender", sender)
                .uniqueResultOptional();

    }

    public Optional<MailLimitation> getMailLimitationById(Session session, long id) {
        return session.createQuery("select mLimit from MailLimitation mLimit " +
                                "where " +
                                "mLimit.id = :id ",
                        MailLimitation.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }

}
