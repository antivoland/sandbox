package antivoland.taltest.model;

import antivoland.taltest.api.dev.domain.EmployeeDetails;

public class Employee {
    public final String id;
    public final String name;

    public Employee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Employee(String id, EmployeeDetails details) {
        this.id = id;
        this.name = details.name;
    }
}
