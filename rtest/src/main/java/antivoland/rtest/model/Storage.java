package antivoland.rtest.model;

public interface Storage<KEY, VALUE extends Versioned> {
    VALUE get(KEY key);

    boolean insert(KEY key, VALUE value);

    boolean update(KEY key, VALUE value);
}
