package ru.antivoland.sandbox.transactions.bitronix;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class PersonService {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    PersonRepository personRepository;

    public void createPersonAndNotify(String id) {
        rabbitTemplate.convertAndSend("persons", id);
        personRepository.add(id);
        if ("error".equals(id)) {
            throw new RuntimeException("Simulated error");
        }
    }
}
