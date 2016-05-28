package antivoland.oftest.model.service;

import antivoland.oftest.model.dao.TileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TileService {
    @Autowired
    TileDAO tileDAO;

    public int population(int y, int x) {
        return tileDAO.population(y, x);
    }
}
