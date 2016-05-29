package antivoland.oftest.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TileDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public double distanceError(int tileY, int tileX) {
        return jdbcTemplate.queryForObject(
                "SELECT distance_error FROM tile WHERE tile_y = ? AND tile_x = ?",
                Double.class,
                tileY,
                tileX
        );
    }
}
