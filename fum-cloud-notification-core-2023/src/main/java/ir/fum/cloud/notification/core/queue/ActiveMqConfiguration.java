package ir.fum.cloud.notification.core.queue;

import ir.fum.cloud.notification.core.configuration.MailPropertiesConfiguration;
import ir.fum.cloud.notification.core.configuration.MailResendPropertiesConfiguration;
import ir.fum.cloud.notification.core.configuration.QueueConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Session;

@Configuration
@RequiredArgsConstructor
public class ActiveMqConfiguration {

    private final QueueConfiguration queueConfiguration;

    private final MailPropertiesConfiguration mailPropertiesConfiguration;

    private final MailResendPropertiesConfiguration mailResendPropertiesConfiguration;


    @Bean
    public PooledConnectionFactory bulkMailConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(mailPropertiesConfiguration.getBulkQueueBrokerUrl());
        activeMQConnectionFactory.setUserName(mailPropertiesConfiguration.getBulkQueueBrokerUsername());
        activeMQConnectionFactory.setPassword(mailPropertiesConfiguration.getBulkQueueBrokerPassword());
        PooledConnectionFactory pooled = new PooledConnectionFactory(activeMQConnectionFactory);
        pooled.setMaxConnections(queueConfiguration.getMaxConnectionPool());

        return pooled;
    }

    @Bean
    public PooledConnectionFactory mailResendConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(mailResendPropertiesConfiguration.getMailResendUrl());
        activeMQConnectionFactory.setUserName(mailResendPropertiesConfiguration.getMailResendUsername());
        activeMQConnectionFactory.setPassword(mailResendPropertiesConfiguration.getMailResendPassword());
        PooledConnectionFactory pooled = new PooledConnectionFactory(activeMQConnectionFactory);
        pooled.setMaxConnections(queueConfiguration.getMaxConnectionPool());

        return pooled;
    }

    @Bean
    public JmsTemplate bulkMailJmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(bulkMailConnectionFactory());
        return jmsTemplate;
    }

    @Bean
    public JmsTemplate mailResendJmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(mailResendConnectionFactory());
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory bulkMailJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(bulkMailConnectionFactory());
        factory.setConcurrency(mailPropertiesConfiguration.getNumberOfBulkConsumers());
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory mailResendJmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(mailResendConnectionFactory());
        factory.setConcurrency(mailResendPropertiesConfiguration.getNumberOfMailResendConsumers());
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }


}