package ru.vi.vitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Worker implements Runnable {
    public static final String ADD = "ADD";
    public static final String REMOVE = "REMOVE";
    public static final String SAVE = "SAVE";
    private static final Logger log = LoggerFactory.getLogger(Worker.class);

    private final Controller controller;
    private final Map<String, Handler> handlers = new HashMap<>();

    public Worker(Controller controller) {
        this.controller = controller;
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
                final JProgressBar bar = controller.getView().createProgressBar(controller.getModel().size());
                try {
                    controller.getModel().save(new Model.ProgressHandler() {
                        @Override
                        public void handleProgress(int progress) {
                            bar.setValue(progress);
                        }
                    });
                } finally {
                    controller.getView().releaseProgressBar(bar);
                }
            }
        });

        while (true) {
            try {
                String command = controller.getCommands().take();
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
