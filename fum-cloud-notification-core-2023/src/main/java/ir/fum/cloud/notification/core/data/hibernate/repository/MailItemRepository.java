package ir.fum.cloud.notification.core.data.hibernate.repository;


import ir.fum.cloud.notification.core.data.hibernate.entity.MailItem;
import ir.fum.cloud.notification.core.data.hibernate.entity.model.RequestState;
import ir.fum.cloud.notification.core.domain.model.vo.UserVO;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MailItemRepository extends BaseRepository<MailItem> {
    public List<MailItem> findByMessageId(Session session, List<String> messageIds) {
        return session.createQuery("select mailItem " +
                                "from MailItem mailItem " +
                                "where " +
                                " mailItem.messageId in :messageIds",
                        MailItem.class)
                .setParameter("messageIds", messageIds)
                .getResultList();
    }

    public List<MailItem> getFailedMails(Session session, UserVO user) {


        List<RequestState> excludeStates = new ArrayList<>();
        excludeStates.add(RequestState.SENT);
        excludeStates.add(RequestState.SCHEDULED);
        excludeStates.add(RequestState.QUEUED);
        excludeStates.add(RequestState.INVALID_DATA);
        excludeStates.add(RequestState.DATA_ERROR);
        excludeStates.add(RequestState.RESEND_QUEUE);

        List<MailItem> list = session.createQuery("select item " +
                                "from MailItem item " +
                                "left join fetch item.mailRequest request " +
                                "left join fetch request.mailConfig config " +
                                "left join fetch request.request model " +
                                "left join fetch request.cc " +
                                "where " +
                                "item.updateTime > :yesterday " +
                                "and " +
                                "model.userId = :user " +
                                "and " +
                                "item.state not in :exclude " +
                                "order by item.updateTime asc"
                        , MailItem.class)
                .setParameter("user", user.getUser_id())
                .setParameter("yesterday", LocalDateTime.now().minusDays(1))
                .setParameter("exclude", excludeStates)
                .list();

        list.forEach(i -> Hibernate.initialize(i.getMailRequest().getBcc()));

        return list;

    }


}
