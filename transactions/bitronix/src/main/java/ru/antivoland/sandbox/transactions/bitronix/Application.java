package ru.antivoland.sandbox.transactions.bitronix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.Closeable;

@ComponentScan
@Configuration
@EnableAutoConfiguration
public class Application {
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
