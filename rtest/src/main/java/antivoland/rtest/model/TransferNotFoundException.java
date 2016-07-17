package antivoland.rtest.model;

public class TransferNotFoundException extends Exception {
    private static final String MESSAGE = "Transfer '%s' not found";

    public TransferNotFoundException(String walletId) {
        super(String.format(MESSAGE, walletId));
    }
}
