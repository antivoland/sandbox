package antivoland.rtest.storage;

import antivoland.rtest.model.Storage;
import antivoland.rtest.model.Versioned;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract class DummyStorage<KEY, VALUE extends Versioned> implements Storage<KEY, VALUE> {
    private static class Wrapper<VALUE extends Versioned> {
        public final VALUE value;

        public Wrapper(VALUE value) {
            this.value = value;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Wrapper<VALUE> wrapper = (Wrapper<VALUE>) o;
            return value.version() == wrapper.value.version();
        }
    }

    private final ConcurrentMap<KEY, Wrapper<VALUE>> storage = new ConcurrentHashMap<>();

    @Override
    public VALUE get(KEY key) {
        Wrapper<VALUE> wrapped = storage.get(key);
        return wrapped != null ? copy(wrapped.value) : null;
    }

    @Override
    public boolean insert(KEY key, VALUE value) {
        Wrapper<VALUE> wrapped = new Wrapper<>(copy(value));
        return storage.putIfAbsent(key, wrapped) == null;
    }

    @Override
    public boolean update(KEY key, VALUE value) {
        Wrapper<VALUE> expecting = new Wrapper<>(copy(value));
        Wrapper<VALUE> wrapped = new Wrapper<>(copy(value));
        wrapped.value.increaseVersion();
        return storage.replace(key, expecting, wrapped);
    }

    protected abstract VALUE copy(VALUE value);
}
