package ru.symbiomark.pos.api.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.symbiomark.pos.api.v1.domain.PointOfSale;
import ru.symbiomark.pos.api.v1.proxy.POSProxy;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api/v1/pos", produces = MediaType.APPLICATION_JSON_VALUE)
public class POS {
    static class Response {
        public final boolean result;
        public final int count;
        public final Collection<PointOfSale> pos;

        private Response(boolean result, int count, Collection<PointOfSale> pos) {
            this.result = result;
            this.count = count;
            this.pos = pos;
        }

        public static Response success(Collection<PointOfSale> pos) {
            return new Response(true, pos.size(), pos);
        }

        public static Response failure() {
            return new Response(false, 0, null);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(POS.class);

    @Autowired
    POSProxy posProxy;

    @RequestMapping(method = RequestMethod.GET)
    public Response list() {
        return Response.success(posProxy.list());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response failure(Exception e) {
        LOG.error("Unhandled error", e);
        return Response.failure();
    }
}
