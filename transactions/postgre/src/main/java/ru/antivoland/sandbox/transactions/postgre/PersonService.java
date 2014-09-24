/**
 * @see <a href="http://www.postgresql.org/docs/9.3/static/transaction-iso.html">PostgreSQL 9.3 Transaction Isolation</a>
 */
package ru.antivoland.sandbox.transactions.postgre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class PersonService {
    private static final Logger log = LoggerFactory.getLogger(PersonService.class);

    private static final RowMapper<Person> PERSON_MAPPER = new RowMapper<Person>() {
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person = new Person();
            person.id = rs.getString("id");
            person.state = rs.getInt("state");
            return person;
        }
    };

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void add(String id) {
        log.info("Adding person " + id);
        jdbcTemplate.update("INSERT INTO person(id) VALUES (?)", id);
    }

    public void addAll(String... ids) {
        for (String person : ids) {
            add(person);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public void updateState(String id) {
        Person person = find(id);
        jdbcTemplate.update("UPDATE person SET state = ? WHERE id = ?", person.state + 1, id);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Person find(String id) {
        return jdbcTemplate.queryForObject("SELECT * from person WHERE id = ?", PERSON_MAPPER, id);
    }

    public List<Person> findAll() {
        return jdbcTemplate.query("SELECT * from person", PERSON_MAPPER);
    }
}
