package com.nhnacademy.mqtt.node;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONException;
import org.json.JSONObject;

import com.nhnacademy.mqtt.CommonsTopicGenerator;
import com.nhnacademy.mqtt.TypeSplitter;
import com.nhnacademy.mqtt.message.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectGenerator extends InputOutputNode {
    CommonsTopicGenerator topicGenerator;
    TypeSplitter typeSplitter;

    public ObjectGenerator(int inputCount, int outputCount) {
        super(inputCount, outputCount);
        topicGenerator = new CommonsTopicGenerator();
        typeSplitter = new TypeSplitter();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message<JSONObject> message = tryGetMessage(); // input 메시지 받아오기
                String topic = topicGenerator.generate(message.getPayload()).toString(); // 토픽 생성

                Map<String, Object> sensorInfo = typeSplitter.generate(message.getPayload()); // 센서 값 받기

                for (Entry<String, Object> entrySet : sensorInfo.entrySet()) {
                    JSONObject o = new JSONObject();
                    o.put("topic", topic + entrySet.getKey());
                    JSONObject subObject = new JSONObject();
                    o.put("time", System.currentTimeMillis() / 1000L);
                    o.put("value", entrySet.getValue());
                    output(0, new Message<JSONObject>(o));
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } catch (JSONException ignore) {
            }
        }
    }
}
