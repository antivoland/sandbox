package antivoland.rtest.model;

public class WalletNotFoundException extends Exception {
    private static final String MESSAGE = "Wallet '%s' not found";

    public WalletNotFoundException(String userId, String currency) {
        super(String.format(MESSAGE, Wallet.id(userId, currency)));
    }
}
