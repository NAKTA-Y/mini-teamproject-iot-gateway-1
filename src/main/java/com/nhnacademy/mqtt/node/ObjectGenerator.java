package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.CommonsTopicGenerator;
import com.nhnacademy.mqtt.TypeSplitter;
import com.nhnacademy.mqtt.message.JsonMessage;
import com.nhnacademy.mqtt.message.Message;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Map.Entry;

@Slf4j
public class ObjectGenerator extends InputOutputNode<Message> {
    private CommonsTopicGenerator topicGenerator;
    private TypeSplitter typeSplitter;

    public ObjectGenerator(int inputCount, int outputCount) {
        super(inputCount, outputCount);
        topicGenerator = new CommonsTopicGenerator();
        typeSplitter = new TypeSplitter();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message message = tryGetMessage();
                JSONObject object = (JSONObject) message.getPayload();

                String topic = topicGenerator.generate(object).toString(); // 토픽 생성

                Map<String, Object> sensorInfo = typeSplitter.generate(object); // 센서 값 받기

                for (Entry<String, Object> entrySet : sensorInfo.entrySet()) {
                    JSONObject o = new JSONObject();
                    o.put("topic", topic + entrySet.getKey());
                    o.put("time", System.currentTimeMillis() / 1000L);
                    o.put("value", entrySet.getValue());
                    output(0, new JsonMessage(o));
                }
            } catch (InterruptedException e) {
                log.error("Thread Error : {}", e.getMessage());
                Thread.currentThread().interrupt();
            } catch (JSONException e) {
                log.warn("JSON Error: {}", e.getMessage());
            }
        }
    }
}
