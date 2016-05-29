package antivoland.oftest.api.dev;

import antivoland.oftest.api.dev.domain.Distance;
import antivoland.oftest.api.dev.domain.Failure;
import antivoland.oftest.model.Tile;
import antivoland.oftest.model.UserPoint;
import antivoland.oftest.model.service.TileNotFoundException;
import antivoland.oftest.model.service.TileService;
import antivoland.oftest.model.service.UserPointNotFoundException;
import antivoland.oftest.model.service.UserPointService;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
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
    private static final String DISTANCE = "Getting distance from point (%s, %s) to user '%s'";
    private static final String NEIGHBORS = "Counting neighbors of point (%s, %s)";

    @Autowired
    UserPointService userPointService;
    @Autowired
    TileService tileService;

    @RequestMapping(method = RequestMethod.GET, value = "{lat:.+}:{lon:.+}/distance-to/{userId}")
    public Distance distance(
            @PathVariable("lat") double lat,
            @PathVariable("lon") double lon,
            @PathVariable("userId") int userId) throws TileNotFoundException, UserPointNotFoundException {

        LOG.debug(String.format(DISTANCE, lat, lon, userId));
        GlobalCoordinates from = new GlobalCoordinates(lat, lon);
        UserPoint userPoint = userPointService.get(userId);
        GlobalCoordinates to = new GlobalCoordinates(userPoint.lat, userPoint.lon);
        GeodeticCurve curve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.WGS84, from, to);

        double distance = curve.getEllipsoidalDistance();

        int tileY = Tile.coordinate(lat);
        int tileX = Tile.coordinate(lon);
        double distanceError = tileService.distanceError(tileY, tileX);
        return distance < distanceError ? Distance.CLOSE : Distance.FAR;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{lat:.+}:{lon:.+}/neighbors")
    public int neighbors(@PathVariable("lat") double lat, @PathVariable("lon") double lon) {
        LOG.debug(String.format(NEIGHBORS, lat, lon));
        return userPointService.neighbors(lat, lon);
    }

    @ExceptionHandler(TileNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Failure tileNotFound(TileNotFoundException e) {
        return new Failure(e);
    }

    @ExceptionHandler(UserPointNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Failure userPointNotFound(UserPointNotFoundException e) {
        return new Failure(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Failure failure(Exception e) {
        return new Failure(e);
    }
}
