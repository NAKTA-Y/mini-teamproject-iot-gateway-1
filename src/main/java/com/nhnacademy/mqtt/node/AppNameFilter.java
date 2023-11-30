package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.checker.Checker;
import com.nhnacademy.mqtt.checker.ValueChecker;
import com.nhnacademy.mqtt.message.Message;

import org.json.JSONObject;

public class AppNameFilter extends InputOutputNode {
    private final Checker valueChecker;

    public AppNameFilter(int inputCount, int outputCount, ValueChecker valueChecker) {
        super(inputCount, outputCount);
        this.valueChecker = valueChecker;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message<JSONObject> receiveMessage = tryGetMessage();
                JSONObject jsonObject = receiveMessage.getPayload();

                if (valueChecker.check(jsonObject.getJSONObject("deviceInfo"))) {
                    Message<JSONObject> sendMessage = new Message<>(jsonObject);
                    output(0, sendMessage);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
