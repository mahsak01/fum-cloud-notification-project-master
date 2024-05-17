package ir.fum.cloud.notification.core.queue;

import ir.fum.cloud.notification.core.domain.model.vo.MailQueueVO;
import ir.fum.cloud.notification.core.domain.service.BulkQueueConnector;
import ir.fum.cloud.notification.core.util.mapper.JsonUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Date;
import java.util.concurrent.ExecutorService;

@Component
public class MailBulkConsumer extends GeneralConsumer implements MessageListener {

    private final BulkQueueConnector connector;

    public MailBulkConsumer(@Qualifier("async.thread.pool") ExecutorService executor,
                            BulkQueueConnector connector) {
        super(executor);
        this.connector = connector;
    }

    @Override
    @JmsListener(destination = "${activemq.mail.bulk.produce : bulk-mail}", containerFactory = "bulkMailJmsListenerContainerFactory")
    public void onMessage(Message message) {
        super.onMessage(message);
    }

    @Override
    void processClientRequest(String clientMessage, Date jmsTimestamp, Date requestDate, String trackerId) {
        MailQueueVO mailQueueVO = JsonUtils.getObject(clientMessage, MailQueueVO.class);

        connector.handleBulkItem(mailQueueVO);

    }
}
