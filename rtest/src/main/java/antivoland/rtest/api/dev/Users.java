package antivoland.rtest.api.dev;

import antivoland.rtest.api.dev.domain.UserDetails;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(value = "/api/dev/users")
public class Users {
    @PUT
    @Path("/{id}")
    public void put(@PathParam("id") String id, UserDetails details) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GET
    @Path("/{id}")
    public UserDetails get(@PathParam("id") String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
