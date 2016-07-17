package antivoland.rtest.model;

public class WalletHasInsufficientFundsException extends Exception {
    private static final String MESSAGE = "Wallet '%s' has insufficient funds";

    public WalletHasInsufficientFundsException(String walletId) {
        super(String.format(MESSAGE, walletId));
    }
}
