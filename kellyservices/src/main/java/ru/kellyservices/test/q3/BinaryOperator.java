package ru.kellyservices.test.q3;

public abstract class BinaryOperator {
    public static BinaryOperator AND = new BinaryOperator() {
        @Override
        public boolean apply(boolean x, boolean y) {
            return x && y;
        }
    };

    public static BinaryOperator OR = new BinaryOperator() {
        @Override
        public boolean apply(boolean x, boolean y) {
            return x || y;
        }
    };

    public abstract boolean apply(boolean x, boolean y);
}
