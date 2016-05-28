package antivoland.oftest.api.dev;

import antivoland.oftest.api.dev.domain.Failure;
import antivoland.oftest.model.Tile;
import antivoland.oftest.model.service.TileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/dev/points", produces = MediaType.APPLICATION_JSON_VALUE)
public class Points {
    private static final Logger LOG = LoggerFactory.getLogger(Points.class);
    private static final String NEIGHBOURHOOD = "Getting neighbourhood of point (%s, %s)";

    @Autowired
    TileService tileService;

    @RequestMapping(method = RequestMethod.GET, value = "{lat:.+}:{lon:.+}/neighbourhood")
    public int neighbourhood(@PathVariable("lat") double lat, @PathVariable("lon") double lon) {
        LOG.debug(String.format(NEIGHBOURHOOD, lat, lon));
        int tileY = Tile.coordinate(lat);
        int tileX = Tile.coordinate(lon);
        return tileService.population(tileY, tileX);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public Failure notImplemented(UnsupportedOperationException e) {
        return new Failure(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Failure failure(Exception e) {
        return new Failure(e);
    }
}
