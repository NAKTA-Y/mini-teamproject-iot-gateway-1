package com.nhnacademy.mqtt.node;

import org.json.JSONObject;

import com.nhnacademy.mqtt.checker.Checker;
import com.nhnacademy.mqtt.message.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NullValueFilter extends InputOutputNode {
    Checker checker;

    public NullValueFilter(int inputCount, int outputCount, Checker checker) {
        super(inputCount, outputCount);
        this.checker = checker;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message<JSONObject> message = tryGetMessage();
                if (checker.check(message.getPayload())) {
                    output(0, message);
                } else {
                    log.error("error message: value 값이 null입니다.");
                }
            } catch (InterruptedException e) {
                log.error("error message", e.getMessage());
            }
        }
    }
}