package antivoland.rtest.model;

import javax.inject.Inject;
import java.math.BigDecimal;

public class TransferService {
    private final TransferStorage transferStorage;
    private final WalletService walletService;

    @Inject
    public TransferService(TransferStorage transferStorage, WalletService walletService) {
        this.transferStorage = transferStorage;
        this.walletService = walletService;
    }

    public void put(String id, String from, String to, String currency, BigDecimal amount) throws TransferAlreadyExistsException {
        Transfer transfer = new Transfer(id, from, to, currency, amount);
        if (!transferStorage.insert(id, transfer)) {
            throw new TransferAlreadyExistsException(id);
        }
    }

    public Transfer get(String id) throws TransferNotFoundException {
        return transferStorage.get(id);
    }

    public void execute(String id) throws TransferNotFoundException, WalletNotFoundException, WalletHasInsufficientFundsException {
        Transfer transfer = get(id);
        walletService.withdraw(transfer.from, transfer.currency, transfer.amount);
        walletService.charge(transfer.to, transfer.currency, transfer.amount);
    }
}
