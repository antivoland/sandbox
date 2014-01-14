package ru.vi.vitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final int SAVE_PERIOD = 600;
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final Random rnd = new Random();
    private static final Map<String, Double> model = new HashMap<>();

    public static void main(String[] args) {
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
                log.info("+n clicked");
                add(Integer.parseInt(txtN.getText()));
            }
        });
        pnlMain.add(btnAdd);

        JButton btnRemove = new JButton("удалить");
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.info("-n clicked");
                remove(Integer.parseInt(txtN.getText()));
            }
        });
        pnlMain.add(btnRemove);

        JLabel lblTime = new JLabel();
        lblTime.setText(String.valueOf(System.currentTimeMillis()));
        pnlMain.add(lblTime);

        f.setVisible(true);
    }

    public void save() {

    }

    public static String dummyKey() {
        long left = ((long) (rnd.nextInt(32)) << 32) + rnd.nextInt(32);
        char[] right = new char[5];
        for (int i = 0; i < right.length; ++i) {
            right[i] = ALPHABET.charAt(rnd.nextInt(ALPHABET.length()));
        }
        StringBuilder buf = new StringBuilder();
        buf.append(left);
        buf.append(right);
        return buf.toString();
    }

    public static Double dummyValue() {
        return rnd.nextDouble();
    }

    public static void add(int n) {
        for (int i = 0; i < n; ++i) {
            String key = dummyKey();
            Double value = dummyValue();
            log.info("key=" + key + ", value=" + dummyValue());
            model.put(key, value);
        }
        log.info("model.size=" + model.size());
    }

    public static void remove(int n) {
        java.util.List<String> keys = new ArrayList<>();
        keys.addAll(model.keySet());

        int i = 0;
        while (i < n && keys.size() > 0) {
            int index = rnd.nextInt(keys.size());
            String key = keys.remove(index);
            model.remove(key);
            ++i;
        }
        log.info("model.size=" + model.size());
    }
}
