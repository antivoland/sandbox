package ru.antivoland.sandbox.transactions.postgre;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@EnableAutoConfiguration
@EnableTransactionManagement
@ComponentScan
@Configuration
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        final PersonService personService = ctx.getBean(PersonService.class);
        personService.addAll("Alice", "Bob", "Carol");
        Assert.assertEquals(3, personService.findAll().size());

        log.info(personService.findAll().toString());

        List<Future> futures = new ArrayList<Future>();
        for (int i = 0; i < 50; ++i) {
            futures.add(executor.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    try {
                        personService.updateState("Bob");
                    } catch (CannotSerializeTransactionException e) {
                        return call();
                    }
                    return null;
                }
            }));
        }

        for (Future future : futures) future.get();
        log.info(personService.findAll().toString());
    }
}
