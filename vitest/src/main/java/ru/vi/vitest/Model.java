package ru.vi.vitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class Model {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SAVE_PERIOD = 10000;
    private static final Random rnd = new Random();
    private static final Logger log = LoggerFactory.getLogger(Model.class);

    private final Map<String, Double> data = new HashMap<>();
    private long nextSaveMillis = System.currentTimeMillis();

    public synchronized void add() {
        String key = dummyKey();
        Double value = dummyValue();
        log.debug("ADD key=" + key + ", value=" + value);
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
        log.debug("REMOVE key=" + key);
        data.remove(key);
    }

    public synchronized void save(ProgressHandler progressHandler) {
        long nowMillis = System.currentTimeMillis();
        if (nowMillis < nextSaveMillis) {
            return;
        }
        nextSaveMillis = (nowMillis / SAVE_PERIOD + 1) * SAVE_PERIOD;
        log.info("next save after " + new Date(nextSaveMillis));
        try {
            serialize(new TreeMap<>(data), progressHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void serialize(Map<String, Double> data, ProgressHandler progressHandler) throws IOException {
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
                log.debug("saved " + off + "B of " + bytes.length + "B");
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
