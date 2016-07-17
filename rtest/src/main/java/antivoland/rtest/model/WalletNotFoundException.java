package antivoland.rtest.model;

public class WalletNotFoundException extends Exception {
    private static final String MESSAGE = "Wallet '%s' not found";

    public WalletNotFoundException(String id) {
        super(String.format(MESSAGE, id));
    }
}
