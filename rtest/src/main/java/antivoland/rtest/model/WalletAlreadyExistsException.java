package antivoland.rtest.model;

public class WalletAlreadyExistsException extends Exception {
    private static final String MESSAGE = "Wallet '%s' already exists";

    public WalletAlreadyExistsException(String id) {
        super(String.format(MESSAGE, id));
    }
}
