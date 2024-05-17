package ir.fum.cloud.notification.core.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MailResendPropertiesConfiguration {
    @Value("${spring.activemq.broker}")
    private String mailResendUrl;
    @Value("${spring.activemq.user}")
    private String mailResendUsername;
    @Value("${spring.activemq.password}")
    private String mailResendPassword;

    @Value("${activemq.mail.resend.producer:resend-mail}")
    private String mailResendProducer;
    @Value("${activemq.number.mail.resend.consumers:5}")
    private String numberOfMailResendConsumers;

    @Value("${email.resend.retry.increment.factor:4}")
    private int tryIncrementFactor;

    @Value("${email.resend.retry.first.interval:5}")
    private int firstRetryInterval;

    @Value("${email.resend.retry.count:3}")
    private int retryLimit;

}
