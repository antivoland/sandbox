package ru.antivoland.xa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;

public class MessageHandler implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    public void onMessage(Message message) {
        log.info("MESSAGE RECEIVED");
    }
}
