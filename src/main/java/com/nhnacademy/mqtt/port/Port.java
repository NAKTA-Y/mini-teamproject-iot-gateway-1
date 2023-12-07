package com.nhnacademy.mqtt.port;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Port<T> {
    BlockingQueue<T> messageQueue;

    public Port() {
        messageQueue = new LinkedBlockingQueue<>();
    }

    public void put(T message) throws InterruptedException {
        messageQueue.put(message);
    }

    public T get() throws InterruptedException {
        return messageQueue.take();
    }
}
