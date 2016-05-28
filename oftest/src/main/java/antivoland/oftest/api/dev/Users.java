package antivoland.oftest.api.dev;

import antivoland.oftest.api.dev.domain.Distance;
import antivoland.oftest.api.dev.domain.Failure;
import antivoland.oftest.api.dev.domain.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/dev/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class Users {
    private static final Logger LOG = LoggerFactory.getLogger(Users.class);
    private static final String DISTANCE_TO = "Getting distance from user %s to point (%s, %s)";
    private static final String CHECKING_IN = "User %s checking in point (%s, %s)";
    private static final String CHECKING_OUT = "User %s checking out";

    @RequestMapping(method = RequestMethod.GET, value = "{id}/distance-to/{lat:.+}:{lon:.+}")
    public Distance distanceTo(@PathVariable("id") int id, @PathVariable("lat") double lat, @PathVariable("lon") double lon) {
        LOG.debug(String.format(DISTANCE_TO, id, lat, lon));
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "{id}/check-in")
    public void checkIn(@PathVariable("id") int id, @RequestBody Point point) {
        LOG.debug(String.format(CHECKING_IN, id, point.lat, point.lon));
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}/check-out")
    public void checkOut(@PathVariable("id") int id) {
        LOG.debug(String.format(CHECKING_OUT, id));
        throw new UnsupportedOperationException("Not implemented yet");
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
