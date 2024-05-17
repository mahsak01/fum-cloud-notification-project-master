package ir.fum.cloud.notification.core.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class DatasourceConfiguration {

    @Value("${project.spring.datasource.hikari.jdbc-url}")
    private String jdbcUrl;
    @Value("${project.spring.datasource.hikari.driver-class-name}")
    private String driverClassName;
    @Value("${project.spring.datasource.hikari.username}")
    private String username;
    @Value("${project.spring.datasource.hikari.password}")
    private String password;
    @Value("${project.spring.datasource.hikari.minimum-idle}")
    private String minimumIdle;
    @Value("${project.spring.datasource.hikari.connectionTimeout}")
    private String connectionTimeout;
    @Value("${project.spring.datasource.hikari.idleTimeout}")
    private String idleTimeout;
    @Value("${project.spring.datasource.hikari.maxLifetime}")
    private String maxLifeTime;
    @Value("${project.spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;
    @Value("${project.spring.datasource.hikari.auto-commit}")
    private String autoCommit;
    @Value("${project.spring.datasource.hikari.leak-detection-threshold}")
    private String lakeDetection;

}
