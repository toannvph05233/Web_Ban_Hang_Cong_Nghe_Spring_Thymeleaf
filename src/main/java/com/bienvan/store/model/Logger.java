package com.bienvan.store.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Logger {
    private static Logger instance;
    private static PrintWriter writer;
    private int hash;

    private Logger() {
        try {
            Random random = new Random();
            hash = random.nextInt(11);
            writer = new PrintWriter(new FileWriter("log.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void remove() {
       instance = null;
    }

    public void log(String message) {
        writer.println(hash + " | " + message);
        writer.flush();
    }

    public void close() {
        writer.close();
    }
}