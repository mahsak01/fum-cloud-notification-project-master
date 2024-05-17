package ir.fum.cloud.notification.core.configuration;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class MailRequestCachePropertiesConfiguration {
    @Value("${mail.request.set.cache: mailRequest}")
    private String mailRequestSet;

    @Value("${mail.request.bin.cache: mailRequest}")
    private String mailRequestBin;

    @Value("${mail.request.expiration.time: 120}")
    private int mailRequestExpiration;


}
