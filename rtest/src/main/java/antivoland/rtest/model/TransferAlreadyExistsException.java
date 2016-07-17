package antivoland.rtest.model;

public class TransferAlreadyExistsException extends Exception {
    private static final String MESSAGE = "Transfer '%s' already exists";

    public TransferAlreadyExistsException(String walletId) {
        super(String.format(MESSAGE, walletId));
    }
}
