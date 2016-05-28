package antivoland.oftest.model.dao;

import antivoland.oftest.model.UserPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserPointDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insert(UserPoint userPoint) {
        jdbcTemplate.update(
                "INSERT INTO user_point(user_id, lat, lon) VALUES (?, ?, ?)",
                userPoint.userId,
                userPoint.lat,
                userPoint.lon
        );
    }

    public int update(UserPoint userPoint) {
        return jdbcTemplate.update(
                "UPDATE user_point SET lat = ?, lon = ? WHERE user_id = ?",
                userPoint.lon,
                userPoint.lat,
                userPoint.userId
        );
    }

    public void delete(int userId) {
        jdbcTemplate.update(
                "DELETE FROM user_point WHERE user_id = ?",
                userId
        );
    }
}
