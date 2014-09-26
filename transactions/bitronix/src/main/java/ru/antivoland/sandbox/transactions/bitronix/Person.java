package ru.antivoland.sandbox.transactions.bitronix;

public class Person {
    public String id;
    public int state;

    @Override
    public String toString() {
        return String.format("%s(%s)", id, state);
    }
}
