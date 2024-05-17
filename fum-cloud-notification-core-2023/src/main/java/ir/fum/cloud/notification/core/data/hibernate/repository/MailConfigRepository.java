package ir.fum.cloud.notification.core.data.hibernate.repository;

import ir.fum.cloud.notification.core.data.hibernate.entity.MailConfig;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author Ali Mojahed on 3/26/2023
 * @project notise
 **/

@Repository
@RequiredArgsConstructor
public class MailConfigRepository {
    private final SessionFactory sessionFactory;

    public void save(Session session, MailConfig mailConfig) {
        session.persist(mailConfig);
    }

    public Optional<MailConfig> getMailConfigByServiceName(Session session,
                                                           String serviceName) {
        return session.createQuery("select mailConfig " +
                                "from MailConfig mailConfig " +
                                "where " +
                                "mailConfig.name = :name " +
                                "and " +
                                "mailConfig.removed = false ",
                        MailConfig.class)
                .setParameter("name", serviceName)
                .uniqueResultOptional();
    }

    public Optional<MailConfig> getMailConfigByServiceName(String serviceName) {
        return sessionFactory.getCurrentSession()
                .createQuery("select mailConfig " +
                                "from MailConfig mailConfig " +
                                "where " +
                                "mailConfig.name = :name " +
                                "and " +
                                "mailConfig.removed = false ",
                        MailConfig.class)
                .setParameter("name", serviceName)
                .uniqueResultOptional();
    }


    public Optional<MailConfig> getMailConfigByOwnerAndServiceName(Session session,
                                                                   String serviceName,
                                                                   long ownerId) {
        return session.createQuery("select mailConfig " +
                                "from MailConfig mailConfig " +
                                "where " +
                                "mailConfig.name = :name " +
                                "and " +
                                "mailConfig.userId = :owner " +
                                "and " +
                                "mailConfig.removed = false ",
                        MailConfig.class)
                .setParameter("name", serviceName)
                .setParameter("owner", ownerId)
                .uniqueResultOptional();
    }


    public Optional<MailConfig> getMailConfigById(Session session, long id, long userId) {
        return session.createQuery("select mailConfig " +
                                "from MailConfig mailConfig " +
                                "where " +
                                "mailConfig.id = :id " +
                                "and " +
                                "mailConfig.userId = :userId " +
                                "and " +
                                "mailConfig.removed = false ",
                        MailConfig.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .uniqueResultOptional();
    }

    @Transactional(value = "projectTransactionManager", rollbackFor = Exception.class)
    public Optional<MailConfig> getMailConfigById(long id) {
        return sessionFactory.getCurrentSession()
                .createQuery("select mailConfig " +
                                "from MailConfig mailConfig " +
                                "where " +
                                "mailConfig.id = :id " +
                                "and " +
                                "mailConfig.removed = false ",
                        MailConfig.class)
                .setParameter("id", id)
                .uniqueResultOptional();
    }

    public void update(Session session, MailConfig mailConfig) {
        session.merge(mailConfig);
    }

    public long getCountMailConfig(@NotNull Session session, long owner) {
        return session.createQuery("select count(mailConfig.id) " +
                                "from MailConfig mailConfig " +
                                "where " +
                                "mailConfig.userId = :owner " +
                                "and " +
                                "mailConfig.removed = false ",
                        Long.class)
                .setParameter("owner", owner)
                .uniqueResult();
    }


    public List<MailConfig> getMailConfigsByOwner(@NotNull Session session, long ownerId, int size, int offset) {
        Query<MailConfig> query = session.createQuery("select mailConfig " +
                                "from MailConfig mailConfig " +
                                "where " +
                                "mailConfig.userId = :owner " +
                                "and " +
                                "mailConfig.removed = false " +
                                "order by mailConfig.id desc ",
                        MailConfig.class)
                .setParameter("owner", ownerId)
                .setFirstResult(offset);

        if (size > 0) {
            query.setMaxResults(size);
        }

        return query.getResultList();
    }

    public void delete(Session session, MailConfig mailConfig) {
        mailConfig.setRemoved(true);
        session.merge(mailConfig);
    }
}
