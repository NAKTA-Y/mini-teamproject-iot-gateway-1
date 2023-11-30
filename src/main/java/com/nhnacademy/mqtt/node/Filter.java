package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.checker.Checker;
import com.nhnacademy.mqtt.message.Message;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class Filter extends InputOutputNode {
    private final Checker keyChekcer;

    public Filter(int inputCount, int outputCount, Checker keyChekcer) {
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
                log.error("Thread error: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
