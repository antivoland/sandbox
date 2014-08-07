package ru.antivoland.xa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@ComponentScan
// @ImportResource("classpath:amqp-sender.xml")
// @RestController
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("xa-exchange");
    }

    @Bean
    Queue queue() {
        return new Queue("xa-queue", true);
    }

    @Bean
    public PlatformTransactionManager transactionManager(ConnectionFactory connectionFactory, DataSource dataSource) {
        RabbitTransactionManager rabbitTransactionManager = new RabbitTransactionManager(connectionFactory);
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource);
        return new ChainedTransactionManager(rabbitTransactionManager, dataSourceTransactionManager);
    }

    public static void main(String[] args) {
        ApplicationContext ctx = org.springframework.boot.SpringApplication.run(Application.class, args);
        JmsTemplate jmsTemplate = ctx.getBean(JmsTemplate.class);
        jmsTemplate.convertAndSend("Hello, Rabbit!");
        log.info("APP STARTED");
    }
}
