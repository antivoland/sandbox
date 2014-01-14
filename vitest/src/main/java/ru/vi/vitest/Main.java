package ru.vi.vitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String ADD = "ADD";
    private static final String REMOVE = "REMOVE";
    private static final String SAVE = "SAVE";
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final BlockingQueue<String> commands = new ArrayBlockingQueue<String>(10);
    private static final Model model = new Model();

    public static void main(String[] args) {
        /*
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            service.submit(new Runnable() {
                public void run() {

                    log.info("saving...");
                }
            });
        }
        */

        JFrame f = new JFrame("MyApp");
        f.setSize(WIDTH, HEIGHT);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new FlowLayout());
        f.add(pnlMain);

        final JTextField txtN = new JTextField();
        txtN.setText("15");
        pnlMain.add(txtN);

        JButton btnAdd = new JButton("добавить");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = Integer.parseInt(txtN.getText());
                for (int i = 0; i < n; ++i) {
                    model.add();
                }
                log.info("model.size=" + model.size());
                model.save();
            }
        });
        pnlMain.add(btnAdd);

        JButton btnRemove = new JButton("удалить");
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = Integer.parseInt(txtN.getText());
                for (int i = 0; i < n; ++i) {
                    model.remove();
                }
                log.info("model.size=" + model.size());
                model.save();
            }
        });
        pnlMain.add(btnRemove);

        JLabel lblTime = new JLabel();
        lblTime.setText(String.valueOf(System.currentTimeMillis()));
        pnlMain.add(lblTime);

        // TODO: progress bar

        f.setVisible(true);
    }
}
