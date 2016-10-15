package antivoland.taltest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Taltest {
    private static final Logger LOG = LoggerFactory.getLogger(Taltest.class);

    @Value("${log.directory}")
    String logDirectory;

    @PostConstruct
    public void starting() {
        LOG.info("Log directory: " + logDirectory);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Taltest.class, args);
    }
}
