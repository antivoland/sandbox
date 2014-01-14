package ru.vi.vitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class Model {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SAVE_PERIOD = 600;
    private static final Random rnd = new Random();
    private static final Logger log = LoggerFactory.getLogger(Model.class);

    private final Map<String, Double> data = new HashMap<>();
    private long lastSaveMillis = -1;

    public synchronized void add() {
        String key = dummyKey();
        Double value = dummyValue();
        // log.info("ADD key=" + key + ", value=" + value);
        data.put(key, value);

    }

    public synchronized void remove() {
        List<String> keys = new ArrayList<>();
        keys.addAll(data.keySet());
        if (keys.isEmpty()) {
            return;
        }

        int index = rnd.nextInt(keys.size());
        String key = keys.remove(index);
        // log.info("REMOVE key=" + key);
        data.remove(key);
    }

    public synchronized TreeMap<String, Double> sortedData() {
        return new TreeMap<>(data);
    }

    public synchronized void save(ProgressHandler progressHandler) {
        // операция автосохранения происходит после каждой модификации, но не чаще раза в 10 секунд
        // после каждой модификации данные сохраняются в файл в сортированном порядке ключа
        // TODO: пока не так
        long nowMillis = System.currentTimeMillis();
        if (nowMillis < lastSaveMillis + SAVE_PERIOD) {
            return;
        }
        lastSaveMillis = nowMillis;
        try {
            serialize(progressHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serialize(ProgressHandler progressHandler) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(data);
        oos.close();

        File f = new File("model.d");
        FileOutputStream fos = new FileOutputStream(f, false);
        try {
            byte[] bytes = baos.toByteArray();
            int chunkSize = bytes.length / data.size();
            int off = 0;
            while (off < bytes.length) {
                int len = Math.min(chunkSize, bytes.length - off);
                fos.write(bytes, off, len);
                off += chunkSize;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.debug("interrupted");
                }
                log.info("saved " + off + "B of " + bytes.length + "B");
                progressHandler.handleProgress(off * data.size() / bytes.length);
            }
        } finally {
            fos.close();
        }
    }

    public synchronized int size() {
        return data.size();
    }

    private static String dummyKey() {
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

    private static Double dummyValue() {
        return rnd.nextDouble();
    }

    public static interface ProgressHandler {
        void handleProgress(int progress);
    }
}
