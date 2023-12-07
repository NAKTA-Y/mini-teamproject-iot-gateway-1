package com.nhnacademy.mqtt.node;

import org.json.JSONException;

import com.nhnacademy.mqtt.checker.Checker;
import com.nhnacademy.mqtt.message.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Filter<T extends Message> extends InputOutputNode<T> {
    private final Checker keyChecker;

    public Filter(int inputCount, int outputCount, Checker keyChecker) {
        super(inputCount, outputCount);
        this.keyChecker = keyChecker;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                T receiveMessage = tryGetMessage();

                if (keyChecker.check(receiveMessage)) {
                    output(0, receiveMessage);
                }

            } catch (InterruptedException e) {
                log.error("Thread Error: {}", e.getMessage());
                Thread.currentThread().interrupt();
            } catch (JSONException e) {
                log.error("JSON Error: {}", e.getMessage());
            }
        }
    }
}
