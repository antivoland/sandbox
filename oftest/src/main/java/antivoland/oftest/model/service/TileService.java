package antivoland.oftest.model.service;

import antivoland.oftest.model.dao.TileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class TileService {
    @Autowired
    TileDAO tileDAO;

    public double distanceError(int tileY, int tileX) throws TileNotFoundException {
        try {
            return tileDAO.distanceError(tileY, tileX);
        } catch (EmptyResultDataAccessException e) {
            throw new TileNotFoundException(tileY, tileX);
        }
    }

    public int population(int tileY, int tileX) {
        return tileDAO.population(tileY, tileX);
    }
}
