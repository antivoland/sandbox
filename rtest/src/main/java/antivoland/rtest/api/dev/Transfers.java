package antivoland.rtest.api.dev;

import antivoland.rtest.api.dev.domain.TransferDetails;
import antivoland.rtest.model.Transfer;
import antivoland.rtest.model.TransferAlreadyExistsException;
import antivoland.rtest.model.TransferNotFoundException;
import antivoland.rtest.model.TransferService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path(value = "/api/dev/transfers")
public class Transfers {
    private final TransferService transferService;

    @Inject
    public Transfers(TransferService transferService) {
        this.transferService = transferService;
    }

    @PUT
    @Path("/{id}")
    public void put(@PathParam("id") String id, TransferDetails details) throws TransferAlreadyExistsException {
        transferService.put(id, details.from, details.to, details.currency, details.amount);
    }

    @GET
    @Path("/{id}")
    public TransferDetails get(@PathParam("id") String id) throws TransferNotFoundException {
        Transfer transfer = transferService.get(id);
        return new TransferDetails(transfer.from, transfer.to, transfer.currency, transfer.amount);
    }
}
