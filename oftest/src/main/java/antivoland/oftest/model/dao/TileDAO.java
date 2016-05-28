package antivoland.oftest.model.dao;

import antivoland.oftest.model.Tile;
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

    public int population(int y, int x) {
        int[] latBounds = Tile.bounds(y);
        int[] lonBounds = Tile.bounds(x);
        return jdbcTemplate.queryForObject(
                "SELECT count(*) FROM user_point WHERE (lat > ? AND lat < ? OR lat = ?) AND (lon > ? AND lon < ? OR lon = ?)",
                Integer.class,
                latBounds[0],
                latBounds[1],
                y,
                lonBounds[0],
                lonBounds[1],
                x
        );
    }
}
