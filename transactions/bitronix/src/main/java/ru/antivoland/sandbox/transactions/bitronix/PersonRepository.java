package ru.antivoland.sandbox.transactions.bitronix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PersonRepository {
    private static final Logger log = LoggerFactory.getLogger(PersonRepository.class);

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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person find(String id) {
        return jdbcTemplate.queryForObject("SELECT * from person WHERE id = ?", PERSON_MAPPER, id);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> findAll() {
        return jdbcTemplate.query("SELECT * from person", PERSON_MAPPER);
    }

    @Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ)
    public void add(String id) {
        log.info(String.format("Adding person %s", id));
        jdbcTemplate.update("INSERT INTO person(id) VALUES (?)", id);
    }

    @Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.REPEATABLE_READ)
    public void updateState(String id, int state) {
        log.info(String.format("Updating person %s with state %s", id, state));
        jdbcTemplate.update("UPDATE person SET state = ? WHERE id = ?", state, id);
    }
}
