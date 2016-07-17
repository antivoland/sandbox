package antivoland.rtest.model;

public class WalletAlreadyExistsException extends Exception {
    private static final String MESSAGE = "Wallet '%s' already exists";

    public WalletAlreadyExistsException(String userId, String currency) {
        super(String.format(MESSAGE, Wallet.id(userId, currency)));
    }
}
