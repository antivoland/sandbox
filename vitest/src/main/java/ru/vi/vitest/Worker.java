package ru.vi.vitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Worker implements Runnable {
    public static final String ADD = "ADD";
    public static final String REMOVE = "REMOVE";
    public static final String SAVE = "SAVE";
    private static final Logger log = LoggerFactory.getLogger(Worker.class);

    private final Controller controller;
    private final BlockingQueue<String> commands;
    private final Map<String, Handler> handlers = new HashMap<>();

    public Worker(Controller controller, BlockingQueue<String> commands) {
        this.controller = controller;
        this.commands = commands;
    }

    @Override
    public void run() {
        handlers.put(ADD, new Handler() {
            @Override
            public void handle() {
                controller.getModel().add();
            }
        });

        handlers.put(REMOVE, new Handler() {
            @Override
            public void handle() {
                controller.getModel().remove();
            }
        });

        handlers.put(SAVE, new Handler() {
            @Override
            public void handle() {
                controller.getModel().save();
            }
        });

        while (true) {
            try {
                String command = commands.take();
                handlers.get(command).handle();
            } catch (InterruptedException e) {
                log.info("interrupted");
            }
        }
    }

    private static interface Handler {
        void handle();
    }
}
