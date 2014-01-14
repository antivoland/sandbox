package ru.vi.vitest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public class Model {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int SAVE_PERIOD = 600;
    private static final Random rnd = new Random();
    private static final Logger log = LoggerFactory.getLogger(Model.class);


    private final Map<String, Double> model = new TreeMap<>();
    private long lastSaveMillis = -1;

    public synchronized void add() {
        String key = dummyKey();
        Double value = dummyValue();
        log.info("ADD key=" + key + ", value=" + value);
        model.put(key, value);

    }

    public synchronized void remove() {
        List<String> keys = new ArrayList<>();
        keys.addAll(model.keySet());
        if (keys.isEmpty()) {
            return;
        }

        int index = rnd.nextInt(keys.size());
        String key = keys.remove(index);
        log.info("REMOVE key=" + key);
        model.remove(key);
    }

    public synchronized void save() {
        // операция автосохранения происходит после каждой модификации, но не чаще раза в 10 секунд
        // после каждой модификации данные сохраняются в файл в сортированном порядке ключа
        long nowMillis = System.currentTimeMillis();
        if (nowMillis < lastSaveMillis + SAVE_PERIOD) {
            return;
        }
        lastSaveMillis = nowMillis;
        log.info("SAVE");
        try {
            serialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void serialize() throws IOException {
        File f = new File("model.d");
        log.info("saving to "+f.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(f, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(model);
        oos.close();
    }

    public synchronized int size() {
        return model.size();
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
}
