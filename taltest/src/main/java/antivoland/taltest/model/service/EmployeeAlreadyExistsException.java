package antivoland.taltest.model.service;

public class EmployeeAlreadyExistsException extends Exception {
    private static final String MESSAGE = "Employee '%s' already exists";

    public EmployeeAlreadyExistsException(String id) {
        super(String.format(MESSAGE, id));
    }
}
