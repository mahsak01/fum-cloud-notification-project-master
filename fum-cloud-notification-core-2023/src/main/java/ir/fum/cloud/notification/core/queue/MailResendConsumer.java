package ir.fum.cloud.notification.core.queue;

import ir.fum.cloud.notification.core.domain.model.vo.MailQueueVO;
import ir.fum.cloud.notification.core.domain.service.MailResendService;
import ir.fum.cloud.notification.core.util.mapper.JsonUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.util.Date;
import java.util.concurrent.ExecutorService;

@Component
public class MailResendConsumer extends GeneralConsumer {

    private final MailResendService connector;

    public MailResendConsumer(@Qualifier("async.thread.pool") ExecutorService executor,
                              MailResendService connector) {
        super(executor);
        this.connector = connector;
    }

    @Override
    @JmsListener(destination = "${activemq.mail.resend.produce:resend-mail}", containerFactory = "mailResendJmsListenerContainerFactory")
    public void onMessage(Message message) {
        super.onMessage(message);
    }

    @Override
    void processClientRequest(String clientMessage, Date jmsTimestamp, Date requestDate, String trackerId) {
        MailQueueVO mailQueueVO = JsonUtils.getObject(clientMessage, MailQueueVO.class);

        connector.handleResendRecord(mailQueueVO);

    }

}
