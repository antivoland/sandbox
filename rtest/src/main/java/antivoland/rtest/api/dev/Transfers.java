package antivoland.rtest.api.dev;

import antivoland.rtest.api.dev.domain.TransferDetails;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(value = "/api/dev/transfers")
public class Transfers {
    @PUT
    @Path("/{id}")
    public void put(@PathParam("id") String id, TransferDetails details) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GET
    @Path("/{id}")
    public TransferDetails get(@PathParam("id") String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
