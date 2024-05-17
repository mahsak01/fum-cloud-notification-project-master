package ir.fum.cloud.notification.core.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DocsConfiguration {

    @Value("${swagger.title:My Project}")
    private String swaggerTitle;
    @Value("${project.private-paths:dummy}")
    private String swaggerPrivatePaths;
    @Value("${docs.private.username:dummy}")
    private String docsPrivateUsername;
    @Value("${docs.private.password:dummy}")
    private String docsPrivatePassword;

}
