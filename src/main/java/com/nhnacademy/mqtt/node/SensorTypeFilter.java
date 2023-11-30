package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.checker.Checker;
import com.nhnacademy.mqtt.message.Message;
import org.json.JSONObject;

public class SensorTypeFilter extends InputOutputNode {
    private final Checker sensorTypeChecker;

    // 생성자 입력 개수, 출력 개수, 센서타입체커
    public SensorTypeFilter(int inputCount, int outputCount, Checker sensorTypChecker) {
        super(inputCount, outputCount);
        this.sensorTypeChecker = sensorTypChecker;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message<JSONObject> receiveMessage = tryGetMessage();
                JSONObject jsonObject = receiveMessage.getPayload();

                // 센서타입체커로 메시지 필드 체크
                if (sensorTypeChecker.check(jsonObject)) {
                    Message<JSONObject> sendMessage = new Message<>(jsonObject);
                    output(0, sendMessage);

                }
                // interruptedexception 발생시 예외 던짐
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
