package antivoland.oftest.api.dev;

import antivoland.oftest.api.dev.domain.Failure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/dev/points", produces = MediaType.APPLICATION_JSON_VALUE)
public class Points {
    private static final Logger LOG = LoggerFactory.getLogger(Points.class);
    private static final String NEIGHBOURHOOD = "Getting neighbourhood of point (%s, %s)";

    @RequestMapping(method = RequestMethod.GET, value = "{lon}:{lat}/neighbourhood")
    public void neighbourhood(@PathVariable("lon") double lon, @PathVariable("lat") double lat) {
        LOG.debug(String.format(NEIGHBOURHOOD, lon, lat));
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
