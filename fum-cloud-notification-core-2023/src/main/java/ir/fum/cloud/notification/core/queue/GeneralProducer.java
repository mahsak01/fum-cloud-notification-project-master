package ir.fum.cloud.notification.core.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Log4j2
@RequiredArgsConstructor
abstract class GeneralProducer {
    private final JmsTemplate jmsTemplate;
    private final String queue;

    @Value("${activemq.charset}")
    private String charset;

    public void sendMessage(String message, Long delay) {
        sendMessage(message, delay, null);
    }

    public void sendMessage(String message, Long delay, String asyncId) {
        try {

            byte[] bytes = message.getBytes(charset);

            log.info("before send: " + asyncId + " " + LocalDateTime.now() + message);

            if (delay == null || delay == 0) {
                jmsTemplate.convertAndSend(queue, bytes);

            } else {

                jmsTemplate.convertAndSend(queue, bytes, m -> {

                    m.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);

                    return m;

                });
            }


        } catch (Exception e) {
            log.error("Sending message failed: {} ,{}", queue, e);
        }
    }
}
