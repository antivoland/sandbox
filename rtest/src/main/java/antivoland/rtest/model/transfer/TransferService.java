package antivoland.rtest.model.transfer;

import antivoland.rtest.model.wallet.WalletHasInsufficientFundsException;
import antivoland.rtest.model.wallet.WalletNotFoundException;
import antivoland.rtest.model.wallet.WalletService;

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
        Transfer transfer = transferStorage.get(id);
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
