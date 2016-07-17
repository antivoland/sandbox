package antivoland.rtest.model;

public interface Storage<KEY, VALUE> {
    VALUE get(KEY key);

    boolean insert(KEY key, VALUE value);

    boolean update(KEY key, VALUE value);
}
