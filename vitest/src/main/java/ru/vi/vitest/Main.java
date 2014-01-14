package ru.vi.vitest;

public class Main {
    private static final int WORKERS = 5;

    public static void main(String[] args) {
        Model model = new Model();
        View view = new View();
        new Controller(model, view, WORKERS).run();
    }
}
