package ir.fum.cloud.notification.core.queue;

import lombok.extern.log4j.Log4j2;

import javax.jms.BytesMessage;
import javax.jms.Message;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Log4j2
abstract public class GeneralConsumer {

    private final ExecutorService executor;

    public GeneralConsumer(ExecutorService executor) {
        this.executor = executor;
    }

    void onMessage(Message message) {
        executor.execute(() -> {
            try {
                String textMessage;
                message.acknowledge();

                if (message instanceof BytesMessage) {
                    BytesMessage bytesMessage = (BytesMessage) message;
                    byte[] data = new byte[(int) bytesMessage.getBodyLength()];
                    bytesMessage.readBytes(data);
                    textMessage = new String(data);

                    processClientRequest(textMessage, new Date(message.getJMSTimestamp()), new Date(), UUID.randomUUID().toString());
                }

            } catch (Exception e) {
                log.error("Received Exception : " + e);

            } finally {
            }
        });


    }

    abstract void processClientRequest(String clientMessage, Date jmsTimestamp, Date requestDate, String notifTrackerId);
}
