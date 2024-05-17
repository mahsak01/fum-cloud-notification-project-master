package ir.fum.cloud.notification.core.data.hibernate.config;

import com.zaxxer.hikari.HikariDataSource;
import ir.fum.cloud.notification.core.configuration.DatasourceConfiguration;
import ir.fum.cloud.notification.core.configuration.HibernateConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Configuration for hibernate
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class HibernateConfig {

    public static int HIBERNATE_LIST_LIMITATION;

    private final HibernateConfiguration hibernateConfiguration;
    private final DatasourceConfiguration datasourceConfiguration;

    private DataSource dataSource;

    @Value("${notification.hibernate.list.limit:100}")
    public void setHibernateListLimitation(int limit) {
        HibernateConfig.HIBERNATE_LIST_LIMITATION = limit;
    }

    @PostConstruct
    private void init() {
        dataSource = createDataSource();
    }


    @Bean("projectDS")
    public DataSource dataSource() {
        return dataSource;
    }

    private DataSource createDataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName(datasourceConfiguration.getDriverClassName());
        hikariDataSource.setJdbcUrl(datasourceConfiguration.getJdbcUrl());
        hikariDataSource.setUsername(datasourceConfiguration.getUsername());
        hikariDataSource.setPassword(datasourceConfiguration.getPassword());
        hikariDataSource.setIdleTimeout(Long.parseLong(datasourceConfiguration.getIdleTimeout()));
        hikariDataSource.setAllowPoolSuspension(false);
        hikariDataSource.setLeakDetectionThreshold(Long.parseLong(datasourceConfiguration.getLakeDetection()));
        hikariDataSource.setMinimumIdle(Integer.parseInt(datasourceConfiguration.getMinimumIdle()));
        hikariDataSource.setPoolName("project_db_pool");
        hikariDataSource.setMaxLifetime(Long.parseLong(datasourceConfiguration.getMaxLifeTime()));
        hikariDataSource.setMaximumPoolSize(datasourceConfiguration.getMaximumPoolSize());

        return hikariDataSource;
    }

    @Bean(name = "projectTransactionManager")
    public PlatformTransactionManager transactionManager1() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(oneSessionFactory().getObject());
        transactionManager.setNestedTransactionAllowed(false);
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }


    @Bean("projectSessionFactory")
    public LocalSessionFactoryBean oneSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("ir.fum.cloud.notification.core.data.hibernate.entity");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    private Properties hibernateProperties() {

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty(
                "hibernate.dialect", hibernateConfiguration.getDialect());
        hibernateProperties.setProperty(
                "hibernate.show_sql", hibernateConfiguration.getShowSql());
        hibernateProperties.setProperty(
                "hibernate.connection.autocommit", datasourceConfiguration.getAutoCommit());
        hibernateProperties.setProperty(
                "hibernate.hikari.maximumPoolSize", String.valueOf(datasourceConfiguration.getMaximumPoolSize()));
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.idleTimeout", datasourceConfiguration.getIdleTimeout());
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.minimumIdle", datasourceConfiguration.getMinimumIdle());
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.connectionTimeout", datasourceConfiguration.getConnectionTimeout());
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.maxLifetime", datasourceConfiguration.getMaxLifeTime());
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.leakDetectionThreshold", datasourceConfiguration.getLakeDetection());
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.cachePrepStmts", "true");
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.prepStmtCacheSize", "250");
        hibernateProperties.setProperty(
                "hibernate.hikari.dataSource.prepStmtCacheSqlLimit", "2048");
        hibernateProperties.setProperty(
                "hibernate.connection.CharSet", "utf8mb4");
        hibernateProperties.setProperty(
                "hibernate.connection.characterEncoding", "utf8mb4");
        hibernateProperties.setProperty(
                "hibernate.connection.useUnicode", "true");

        return hibernateProperties;
    }

}
