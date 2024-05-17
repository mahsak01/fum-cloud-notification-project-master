package ir.fum.cloud.notification.core.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class QueueConfiguration {

    @Value("${async.ttl}")
    private Long ttl;
    @Value("${activemq.charset}")
    private String charset;
    @Value("${activemq-max-connection-pool}")
    private int maxConnectionPool;

}
