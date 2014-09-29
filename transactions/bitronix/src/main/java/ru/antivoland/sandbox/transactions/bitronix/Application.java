package ru.antivoland.sandbox.transactions.bitronix;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

import java.io.Closeable;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class Application {
    @Bean
    DirectExchange exchange() {
        return new DirectExchange("sandbox");
    }

    @Bean
    Queue queue() {
        return new Queue("persons", true);
    }

    @Bean
    Binding queueBinding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).withQueueName();
    }

    @Bean
    SimpleMessageListenerContainer queueListener(ConnectionFactory cf) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cf);
        container.setQueueNames("persons");
        container.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("----> " + new String(message.getBody()));
            }
        });
        container.setErrorHandler(new ErrorHandler() {
            @Override
            public void handleError(Throwable t) {
                t.printStackTrace();
            }
        });
        return container;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        PersonService service = context.getBean(PersonService.class);
        PersonRepository repository = context.getBean(PersonRepository.class);
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
    }
}
