package antivoland.rtest.api.dev;

import antivoland.rtest.api.dev.domain.Failure;
import antivoland.rtest.api.dev.domain.WalletDetails;
import antivoland.rtest.model.wallet.Wallet;
import antivoland.rtest.model.wallet.WalletAlreadyExistsException;
import antivoland.rtest.model.wallet.WalletNotFoundException;
import antivoland.rtest.model.wallet.WalletService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(value = "/api/dev/wallets/{id}")
public class Wallets {
    private final WalletService walletService;

    @Inject
    public Wallets(WalletService walletService) {
        this.walletService = walletService;
    }

    @PUT
    public void put(@PathParam("id") String id, WalletDetails details) {
        try {
            walletService.put(id, details.currency, details.balance);
        } catch (WalletAlreadyExistsException e) {
            throw new Failure(e).withStatus(Response.Status.CONFLICT);
        }
    }

    @GET
    public WalletDetails get(@PathParam("id") String id) {
        Wallet wallet;
        try {
            wallet = walletService.get(id);
        } catch (WalletNotFoundException e) {
            throw new Failure(e).withStatus(Response.Status.NOT_FOUND);
        }
        return new WalletDetails(wallet.currency, wallet.balance);
    }
}
