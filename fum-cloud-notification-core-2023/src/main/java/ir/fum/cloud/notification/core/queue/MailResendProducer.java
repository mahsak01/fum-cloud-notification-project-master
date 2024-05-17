package ir.fum.cloud.notification.core.queue;

import ir.fum.cloud.notification.core.configuration.MailResendPropertiesConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


@Component
public class MailResendProducer extends GeneralProducer {
    public MailResendProducer(@Qualifier("mailResendJmsTemplate") JmsTemplate jmsTemplate,
                              MailResendPropertiesConfiguration mailPropertiesConfiguration) {
        super(jmsTemplate, mailPropertiesConfiguration.getMailResendProducer());
    }
}
