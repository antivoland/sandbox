package antivoland.rtest.model;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransferService {
    private final Map<String, Transfer> transfers = new HashMap<>();
    private final WalletService walletService;

    @Inject
    public TransferService(WalletService walletService) {
        this.walletService = walletService;
    }

    public void put(String id, String from, String to, String currency, BigDecimal amount) throws TransferAlreadyExistsException {
        if (transfers.containsKey(id)) {
            throw new TransferAlreadyExistsException(id);
        }
        transfers.put(id, new Transfer(id, from, to, currency, amount));
    }

    public Transfer get(String id) throws TransferNotFoundException {
        Transfer transfer = transfers.get(id);
        if (transfer == null) {
            throw new TransferNotFoundException(id);
        }
        return transfer;
    }

    public void execute(String id) throws TransferNotFoundException, WalletNotFoundException, WalletHasInsufficientFundsException {
        Transfer transfer = get(id);
        walletService.withdraw(transfer.from, transfer.currency, transfer.amount);
        walletService.charge(transfer.to, transfer.currency, transfer.amount);
    }
}
