package ru.antivoland.sandbox.transactions.bitronix;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Messages {
    @JmsListener(destination = "persons")
    public void onMessage(String content) {
        System.out.println("----> " + content);
    }
}
