package ir.fum.cloud.notification.core.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MailPropertiesConfiguration {

    @Value("${mail.config.default.service:projectDefault}")
    private String defaultMailConfigServiceName;

    @Value("${mail.bulk.limit : 5}")
    private int bulkLimitation;

    @Value("${mail.one.minute.limit: 10}")
    private int mailSendingPerMinuteLimit;

    @Value("${attachment.style :  }")
    private String attachmentStyle;

    @Value("${file.style:  } ")
    private String fileStyle;

    @Value("${spring.activemq.broker}")
    private String bulkQueueBrokerUrl;
    @Value("${spring.activemq.user}")
    private String bulkQueueBrokerUsername;
    @Value("${spring.activemq.password}")
    private String bulkQueueBrokerPassword;

    @Value("${activemq.mail.bulk.producer: bulk-mail}")
    private String bulkQueueProducer;
    @Value("${activemq.number.mail.bulk.consumers:5}")
    private String numberOfBulkConsumers;


}
