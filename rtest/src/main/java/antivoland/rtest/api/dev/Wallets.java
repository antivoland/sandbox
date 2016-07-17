package antivoland.rtest.api.dev;

import antivoland.rtest.api.dev.domain.WalletDetails;
import antivoland.rtest.model.Wallet;
import antivoland.rtest.model.WalletAlreadyExistsException;
import antivoland.rtest.model.WalletNotFoundException;
import antivoland.rtest.model.WalletService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(value = "/api/dev/users/{userId}/wallets")
public class Wallets {
    private final WalletService walletService;

    @Inject
    public Wallets(WalletService walletService) {
        this.walletService = walletService;
    }

    @PUT
    @Path("/{currency}")
    public void put(@PathParam("userId") String userId, @PathParam("currency") String currency, WalletDetails details) throws WalletAlreadyExistsException {
        walletService.put(userId, currency, details.balance);
    }

    @GET
    @Path("/{currency}")
    public WalletDetails get(@PathParam("userId") String userId, @PathParam("currency") String currency) throws WalletNotFoundException {
        Wallet wallet = walletService.get(userId, currency);
        return new WalletDetails(wallet.balance);
    }

    @DELETE
    @Path("/{currency}")
    public void delete(@PathParam("userId") String userId, @PathParam("currency") String currency) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
