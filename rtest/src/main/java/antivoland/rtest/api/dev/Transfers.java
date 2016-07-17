package antivoland.rtest.api.dev;

import antivoland.rtest.api.dev.domain.Failure;
import antivoland.rtest.api.dev.domain.TransferDetails;
import antivoland.rtest.model.transfer.Transfer;
import antivoland.rtest.model.transfer.TransferAlreadyExistsException;
import antivoland.rtest.model.transfer.TransferNotFoundException;
import antivoland.rtest.model.transfer.TransferService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public void put(@PathParam("id") String id, TransferDetails details) {
        try {
            transferService.put(id, details.from, details.to, details.currency, details.amount);
        } catch (TransferAlreadyExistsException e) {
            throw new Failure(e).withStatus(Response.Status.CONFLICT);
        }
    }

    @GET
    @Path("/{id}")
    public TransferDetails get(@PathParam("id") String id) {
        Transfer transfer;
        try {
            transfer = transferService.get(id);
        } catch (TransferNotFoundException e) {
            throw new Failure(e).withStatus(Response.Status.NOT_FOUND);
        }
        return new TransferDetails(transfer.from, transfer.to, transfer.currency, transfer.amount);
    }
}
