package ir.fum.cloud.notification.core.data.hibernate.repository;

import org.hibernate.Session;

public class BaseRepository<T> {
    public void save(Session session, T entity) {
        session.persist(entity);
    }

    public void update(Session session, T entity) {
        session.merge(entity);
    }

}
