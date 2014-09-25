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
        AccountService service = context.getBean(AccountService.class);
        AccountRepository repository = context.getBean(AccountRepository.class);
        service.createAccountAndNotify("josh");
        System.out.println("Count is " + repository.count());
        try {
            service.createAccountAndNotify("error");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Count is " + repository.count());
        Thread.sleep(100);
        ((Closeable) context).close();
    }
}
