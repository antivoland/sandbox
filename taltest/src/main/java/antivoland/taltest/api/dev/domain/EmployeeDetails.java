package antivoland.taltest.api.dev.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

// todo: extend details with ability to sign docs
public class EmployeeDetails {
    public final String name;

    public EmployeeDetails(@JsonProperty("name") String name) {
        this.name = name;
    }
}
