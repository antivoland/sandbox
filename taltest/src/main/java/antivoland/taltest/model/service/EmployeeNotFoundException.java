package antivoland.taltest.model.service;

public class EmployeeNotFoundException extends Exception {
    private static final String MESSAGE = "Employee '%s' not found";

    public EmployeeNotFoundException(String id) {
        super(String.format(MESSAGE, id));
    }
}
