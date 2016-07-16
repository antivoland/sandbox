package antivoland.rtest.model;

public class User {
    public enum Gender {male, female}

    public final String id;
    public final Gender gender;

    public User(String id, Gender gender) {
        this.id = id;
        this.gender = gender;
    }
}
