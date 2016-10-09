package antivoland.taltest.model.dao;

import antivoland.taltest.model.Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RelationDAO {
    private static final RowMapper<Relation> ROW_MAPPER = (rs, i) -> new Relation(
            rs.getString("manager_id"),
            rs.getString("employee_id")
    );

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public List<Relation> list() {
        return jdbcTemplate.query("SELECT * FROM relation", ROW_MAPPER);
    }

    public void insert(Relation relation) {
        Map<String, Object> params = new HashMap<>();
        params.put("managerId", relation.managerId);
        params.put("employeeId", relation.employeeId);
        jdbcTemplate.update("INSERT INTO relation(manager_id, employee_id) VALUES (:managerId, :employeeId)", params);
    }

    public int delete(String managerId, String employeeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("managerId", managerId);
        params.put("employeeId", employeeId);
        return jdbcTemplate.update("DELETE FROM relation WHERE manager_id = :managerId AND employee_id = :employeeId", params);
    }

    public void deleteAll(String employeeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        jdbcTemplate.update("DELETE FROM relation WHERE manager_id = :employeeId OR employee_id = :employeeId", params);
    }
}
