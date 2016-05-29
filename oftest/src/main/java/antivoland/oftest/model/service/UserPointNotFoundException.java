package antivoland.oftest.model.service;

public class UserPointNotFoundException extends Exception {
    private static final String MESSAGE = "Point not found for user '%s'";

    public UserPointNotFoundException(int userId) {
        super(String.format(MESSAGE, userId));
    }
}
