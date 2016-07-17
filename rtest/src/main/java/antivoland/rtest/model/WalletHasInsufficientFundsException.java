package antivoland.rtest.model;

public class WalletHasInsufficientFundsException extends Exception {
    private static final String MESSAGE = "Wallet '%s' has insufficient funds";

    public WalletHasInsufficientFundsException(String id) {
        super(String.format(MESSAGE, id));
    }
}
