package ru.antivoland.sandbox.transactions.bitronix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PersonService {
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    PersonRepository personRepository;

    public void createPersonAndNotify(String id) {
        this.jmsTemplate.convertAndSend("persons", id);
        this.personRepository.add(id);
        if ("error".equals(id)) {
            throw new RuntimeException("Simulated error");
        }
    }
}
