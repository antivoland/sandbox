package antivoland.oftest.model.service;

public class UserPointAlreadyExistsException extends Exception {
    private static final String MESSAGE = "Point already exists for user '%s'";

    public UserPointAlreadyExistsException(int userId) {
        super(String.format(MESSAGE, userId));
    }
}
