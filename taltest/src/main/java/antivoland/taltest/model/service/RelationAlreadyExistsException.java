package antivoland.taltest.model.service;

import antivoland.taltest.model.Relation;

public class RelationAlreadyExistsException extends Exception {
    private static final String MESSAGE = "Relation between manager '%s' and employee '%s' already exists";

    public RelationAlreadyExistsException(String managerId, String employeeId) {
        super(String.format(MESSAGE, managerId, employeeId));
    }
}
