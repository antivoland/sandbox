package ru.vi.vitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    private final Model model;
    private final View view;
    private final ExecutorService service;
    private final BlockingQueue<String> commands = new LinkedBlockingQueue<>();

    public Controller(Model model, View view, int workers) {
        this.model = model;
        this.view = view;
        this.service = Executors.newFixedThreadPool(workers);

        for (int i = 0; i < workers; ++i) {
            service.submit(new Worker(this));
        }
    }

    public Model getModel() {
        return model;
    }

    public View getView() {
        return view;
    }

    public BlockingQueue<String> getCommands() {
        return commands;
    }

    public void run() {
        view.btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = Integer.parseInt(view.txtN.getText());
                for (int i = 0; i < n; ++i) {
                    commands.add(Worker.ADD);
                }
                save();
            }
        });

        view.btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = Integer.parseInt(view.txtN.getText());
                for (int i = 0; i < n; ++i) {
                    commands.add(Worker.REMOVE);
                }
                save();
            }
        });

        view.run();

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                view.lblTime.setText(String.valueOf(System.currentTimeMillis()));
                log.info("model.size=" + model.size());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void save() {
        commands.add(Worker.SAVE);
    }
}
