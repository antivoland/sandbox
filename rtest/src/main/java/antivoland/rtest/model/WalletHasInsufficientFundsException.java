package antivoland.rtest.model;

public class WalletHasInsufficientFundsException extends Exception {
    private static final String MESSAGE = "Wallet '%s' has insufficient funds";

    public WalletHasInsufficientFundsException(String userId, String currency) {
        super(String.format(MESSAGE, Wallet.id(userId, currency)));
    }
}
