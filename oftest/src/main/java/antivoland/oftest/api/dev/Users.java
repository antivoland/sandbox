package antivoland.oftest.api.dev;

import antivoland.oftest.api.dev.domain.Distance;
import antivoland.oftest.api.dev.domain.Failure;
import antivoland.oftest.api.dev.domain.Point;
import antivoland.oftest.model.service.UserPointAlreadyExistsException;
import antivoland.oftest.model.service.UserPointNotFoundException;
import antivoland.oftest.model.service.UserPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/dev/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class Users {
    private static final Logger LOG = LoggerFactory.getLogger(Users.class);
    private static final String DISTANCE_TO = "Getting distance from user '%s' to point (%s, %s)";
    private static final String CREATE = "Creating point (%s, %s) for user '%s'";
    private static final String TRACK = "Updating point with (%s, %s) for user '%s'";
    private static final String REMOVE = "Removing point for user '%s'";

    @Autowired
    UserPointService userPointService;

    @RequestMapping(method = RequestMethod.GET, value = "{id}/distance-to/{lat:.+}:{lon:.+}")
    public Distance distanceTo(@PathVariable("id") int id, @PathVariable("lat") double lat, @PathVariable("lon") double lon) {
        LOG.debug(String.format(DISTANCE_TO, id, lat, lon));
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public void create(@PathVariable("id") int id, @RequestBody Point point) throws UserPointAlreadyExistsException {
        LOG.debug(String.format(CREATE, id, point.lat, point.lon));
        userPointService.create(id, point.lat, point.lon);
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void track(@PathVariable("id") int id, @RequestBody Point point) throws UserPointNotFoundException {
        LOG.debug(String.format(TRACK, id, point.lat, point.lon));
        userPointService.track(id, point.lat, point.lon);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public void remove(@PathVariable("id") int id) {
        LOG.debug(String.format(REMOVE, id));
        userPointService.remove(id);
    }

    @ExceptionHandler(UserPointNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Failure userPointNotFound(UserPointNotFoundException e) {
        return new Failure(e);
    }

    @ExceptionHandler(UserPointAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Failure userPointAlreadyExists(UserPointAlreadyExistsException e) {
        return new Failure(e);
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
