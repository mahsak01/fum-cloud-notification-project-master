package ir.fum.cloud.notification.core.queue;

import ir.fum.cloud.notification.core.configuration.MailPropertiesConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MailBulkProducer extends GeneralProducer {


    public MailBulkProducer(@Qualifier("bulkMailJmsTemplate") JmsTemplate jmsTemplate, MailPropertiesConfiguration mailPropertiesConfiguration) {
        super(jmsTemplate, mailPropertiesConfiguration.getBulkQueueProducer());
    }
}
