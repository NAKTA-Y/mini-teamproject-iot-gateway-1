package com.nhnacademy.mqtt.node;

import java.nio.charset.Charset;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONObject;

import com.nhnacademy.mqtt.message.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttSubscriber extends OutputNode {
    private static final String TOPIC_FILTER = "application/#";
    private MqttClient client;
    private MqttConnectOptions options;

    public MqttSubscriber(int outputCount, MqttClient client) {
        super(outputCount);
        this.client = client;
    }

    public MqttSubscriber(int outputCount, MqttClient client, MqttConnectOptions options) {
        super(outputCount);
        this.client = client;
        this.options = options;
    }

    @Override
    public void run() {
        // TODO
        try {
            if (options == null) {
                client.connect();
            } else {
                client.connect(options);
            }

            // subscribe 생성
            client.subscribe(TOPIC_FILTER, (topic, msg) -> {
                // JSONObject 생성
                JSONObject object = new JSONObject(new String(msg.getPayload()));
                Message<JSONObject> message = new Message<>(object);

                // Output
                output(0, message);
            });

        } catch (MqttException e) {
            log.error("error MQTT message : {}", e.getMessage());
        } catch (Exception e) {
            log.error("error : {}", e.getMessage());
        }
    }
}
