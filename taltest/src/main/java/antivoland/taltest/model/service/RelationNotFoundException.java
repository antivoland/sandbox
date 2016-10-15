package antivoland.taltest.model.service;

public class RelationNotFoundException extends Exception {
    private static final String MESSAGE = "Relation between manager '%s' and employee '%s' not found";

    public RelationNotFoundException(String managerId, String employeeId) {
        super(String.format(MESSAGE, managerId, employeeId));
    }
}
