package antivoland.rtest.api.dev.domain;

public class Failure {
    public final String message;

    public Failure(Exception e) {
        this.message = e.getMessage();
    }
}
