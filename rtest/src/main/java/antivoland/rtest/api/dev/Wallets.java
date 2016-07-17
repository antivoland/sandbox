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
@Path(value = "/api/dev/wallets")
public class Wallets {
    private final WalletService walletService;

    @Inject
    public Wallets(WalletService walletService) {
        this.walletService = walletService;
    }

    @PUT
    @Path("/{id}")
    public void put(@PathParam("id") String id, WalletDetails details) throws WalletAlreadyExistsException {
        walletService.put(id, details.currency, details.balance);
    }

    @GET
    @Path("/{id}")
    public WalletDetails get(@PathParam("id") String id) throws WalletNotFoundException {
        Wallet wallet = walletService.get(id);
        return new WalletDetails(wallet.currency, wallet.balance);
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
