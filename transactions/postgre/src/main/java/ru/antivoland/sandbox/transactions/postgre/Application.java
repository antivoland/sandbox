package ru.antivoland.sandbox.transactions.postgre;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.CannotSerializeTransactionException;
import org.springframework.stereotype.Component;
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
@Component
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Autowired
    PersonService personService;

    private List<Future> submitPersonUpdates(final String id, int count) {
        List<Future> futures = new ArrayList<Future>();
        for (int i = 0; i < count; ++i) {
            futures.add(executor.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    try {
                        personService.nextState(id);
                    } catch (CannotSerializeTransactionException e) {
                        return call();
                    }
                    return null;
                }
            }));
        }
        return futures;
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        final PersonService personService = ctx.getBean(PersonService.class);
        personService.addAll("Alice", "Bob", "Carol");
        Assert.assertEquals(3, personService.findAll().size());
        log.info(personService.findAll().toString());

        long startMillis = System.currentTimeMillis();
        Application application = ctx.getBean(Application.class);
        List<Future> futures = new ArrayList<Future>();
        futures.addAll(application.submitPersonUpdates("Alice", 40));
        futures.addAll(application.submitPersonUpdates("Bob", 30));
        futures.addAll(application.submitPersonUpdates("Carol", 50));

        for (Future future : futures) future.get();
        log.info(personService.findAll().toString());
        log.info(String.format("Elapsed time: %sms", System.currentTimeMillis() - startMillis));

        Assert.assertEquals(40, personService.find("Alice").state);
        Assert.assertEquals(30, personService.find("Bob").state);
        Assert.assertEquals(50, personService.find("Carol").state);
    }
}
