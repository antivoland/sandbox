package antivoland.rtest.model;

public interface Versioned {
    int SINCE = 0;

    long version();

    void increaseVersion();
}
