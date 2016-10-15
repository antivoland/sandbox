package antivoland.taltest.model.dao;

import antivoland.taltest.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDAO {
    private static final RowMapper<Employee> ROW_MAPPER = (rs, i) -> new Employee(
            rs.getString("id"),
            rs.getString("name")
    );

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public List<Employee> list() {
        return jdbcTemplate.query("SELECT * FROM employee", ROW_MAPPER);
    }

    public void insert(Employee employee) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", employee.id);
        params.put("name", employee.name);
        jdbcTemplate.update("INSERT INTO employee(id, name) VALUES (:id, :name)", params);
    }

    public int update(Employee employee) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", employee.id);
        params.put("name", employee.name);
        return jdbcTemplate.update("UPDATE employee SET name = :name WHERE id = :id", params);
    }

    public int delete(String id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbcTemplate.update("DELETE FROM employee WHERE id = :id", params);
    }
}
