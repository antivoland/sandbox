package ru.vi.vitest;

import javax.swing.*;
import java.awt.*;

public class View {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    public final JTextField txtN = new JTextField();
    public final JButton btnAdd = new JButton("добавить");
    public final JButton btnRemove = new JButton("удалить");
    public final JLabel lblTime = new JLabel();
    // public final JProgressBar prgSave = new JProgressBar();
    private final JPanel pnlMain = new JPanel();

    public void run() {
        JFrame f = new JFrame("MyApp");
        f.setSize(WIDTH, HEIGHT);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pnlMain.setLayout(new FlowLayout());
        f.add(pnlMain);

        txtN.setText("15");
        pnlMain.add(txtN);

        pnlMain.add(btnAdd);
        pnlMain.add(btnRemove);
        pnlMain.add(lblTime);

        // pnlMain.add(prgSave);

        f.setVisible(true);
    }

    public JProgressBar createProgressBar(int modelSize) {
        JProgressBar bar = new JProgressBar(SwingConstants.HORIZONTAL, 1, modelSize);
        pnlMain.add(bar);
        return bar;

    }

    public void releaseProgressBar(JProgressBar bar) {
        pnlMain.remove(bar);
    }
}
