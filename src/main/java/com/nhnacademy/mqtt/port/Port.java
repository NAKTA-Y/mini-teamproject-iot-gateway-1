package com.nhnacademy.mqtt.port;

import com.nhnacademy.mqtt.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Port {
    BlockingQueue<Message> messageQueue;

    public Port() {
        messageQueue = new LinkedBlockingQueue<>();
    }

    public void put(Message message) throws InterruptedException {
        messageQueue.put(message);
    }

    public Message get() throws InterruptedException {
        return messageQueue.take();
    }
}
