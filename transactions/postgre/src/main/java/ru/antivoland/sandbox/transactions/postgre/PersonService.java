/**
 * @see <a href="http://www.postgresql.org/docs/9.3/static/transaction-iso.html">PostgreSQL 9.3 Transaction Isolation</a>
 */
package ru.antivoland.sandbox.transactions.postgre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;

    public Person find(String id) {
        return personRepository.find(id);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public void addAll(String... ids) {
        personRepository.addAll(ids);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public void nextState(String id) {
        Person person = personRepository.find(id);
        personRepository.updateState(id, person.state + 1);
        // do business
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
