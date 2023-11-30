package com.nhnacademy.mqtt.port;

import com.nhnacademy.mqtt.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

public class Port {
    BlockingQueue<Message<JSONObject>> messageQueue;

    public Port() {
        messageQueue = new LinkedBlockingQueue<>();
    }

    public void put(Message<JSONObject> message) throws InterruptedException {
        messageQueue.put(message);
    }

    public Message<JSONObject> get() throws InterruptedException {
        return messageQueue.take();
    }
}
