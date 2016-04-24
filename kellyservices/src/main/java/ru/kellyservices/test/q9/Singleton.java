package ru.kellyservices.test.q9;

public class Singleton {
    private static class Lazy {
        public static final Singleton INSTANCE = new Singleton();
    }

    private Singleton() {}

    public static Singleton getInstance() {
        return Lazy.INSTANCE;
    }
}
