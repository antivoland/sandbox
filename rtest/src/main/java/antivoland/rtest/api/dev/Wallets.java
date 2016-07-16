package antivoland.rtest.api.dev;

import antivoland.rtest.api.dev.domain.WalletDetails;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(value = "/api/dev/users/{userId}/wallets")
public class Wallets {
    @PUT
    @Path("/{currency}")
    public void put(@PathParam("userId") String userId, @PathParam("currency") String currency, WalletDetails details) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GET
    @Path("/{currency}")
    public WalletDetails get(@PathParam("userId") String userId, @PathParam("currency") String currency) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @DELETE
    @Path("/{currency}")
    public void delete(@PathParam("userId") String userId, @PathParam("currency") String currency) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
