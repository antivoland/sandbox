package antivoland.rtest.api.dev.domain;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class Failure {
    public final String message;

    public Failure(Exception e) {
        this.message = e.getMessage();
    }

    public WebApplicationException withStatus(Response.Status status) {
        return new WebApplicationException(Response.status(status).entity(this).build());
    }
}
