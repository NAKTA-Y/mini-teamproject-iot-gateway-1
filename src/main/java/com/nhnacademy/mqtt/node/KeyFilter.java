package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.checker.Checker;
import com.nhnacademy.mqtt.message.Message;
import org.json.JSONObject;

public class KeyFilter extends InputOutputNode {
    private final Checker keyChekcer;
    public KeyFilter(int inputCount, int outputCount, Checker keyChekcer) {
        super(inputCount, outputCount);
        this.keyChekcer = keyChekcer;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message<JSONObject> receiveMessage = tryGetMessage();

                JSONObject jsonObject = receiveMessage.getPayload();

                if (keyChekcer.check(jsonObject)) {
                    Message<JSONObject> sendMessage = new Message<>(jsonObject);
                    output(0, sendMessage);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
