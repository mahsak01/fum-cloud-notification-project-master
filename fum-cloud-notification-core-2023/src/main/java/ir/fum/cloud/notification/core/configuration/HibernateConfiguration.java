package ir.fum.cloud.notification.core.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class HibernateConfiguration {

    @Value("${project.spring.jpa.hibernate.ddl-auto:update}")
    private String ddlAuto;
    @Value("${project.spring.jpa.hibernate.dialect}")
    private String dialect;
    @Value("${project.spring.jpa.show-sql}")
    private String showSql;

}
