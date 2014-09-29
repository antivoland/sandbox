package ru.antivoland.sandbox.transactions.bitronix;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ErrorHandler;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class Application {
    @Bean
    DirectExchange exchange() {
        return new DirectExchange("sandbox");
    }

    @Bean
    @Qualifier("commands")
    Queue commandsQueue() {
        return new Queue("commands", true);
    }

    @Bean
    Binding commandsQueueBinding(@Qualifier("commands") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).withQueueName();
    }

    @Bean
    @Qualifier("persons")
    Queue queue() {
        return new Queue("persons", true);
    }

    @Bean
    Binding personsQueueBinding(@Qualifier("persons") Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).withQueueName();
    }

    @Bean
    @Qualifier("commands")
    MessageListener commandsMessageListener(final PersonService service) {
        return new MessageListener() {
            @Override
            public void onMessage(Message message) {
                String person = new String(message.getBody());
                service.createPersonAndNotify(person);
            }
        };
    }

    @Bean
    SimpleMessageListenerContainer commandsQueueListener(
            ConnectionFactory cf,
            PlatformTransactionManager ptm,
            @Qualifier("commands") MessageListener commandsMessageListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cf);
        container.setQueueNames("commands");
        container.setMessageListener(commandsMessageListener);
        container.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                t.printStackTrace();
            }
        });
        container.setTransactionManager(ptm);
        container.setChannelTransacted(true);
        return container;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        PersonService service = context.getBean(PersonService.class);
        PersonRepository repository = context.getBean(PersonRepository.class);

        RabbitTemplate rabbitTemplate = context.getBean(RabbitTemplate.class);
        rabbitTemplate.setChannelTransacted(true);

        rabbitTemplate.convertAndSend("commands", "josh");
        rabbitTemplate.convertAndSend("commands", "error");

        /*
        service.createPersonAndNotify("josh");
        System.out.println("Count is " + repository.findAll().size());
        try {
            service.createPersonAndNotify("error");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Count is " + repository.findAll().size());
        Thread.sleep(100);
        ((Closeable) context).close();
        */
    }
}
