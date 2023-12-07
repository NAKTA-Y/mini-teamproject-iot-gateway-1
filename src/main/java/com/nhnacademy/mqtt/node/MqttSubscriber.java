package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.message.JsonMessage;
import com.nhnacademy.mqtt.message.Message;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

@Slf4j
public class MqttSubscriber extends OutputNode<Message> {
    private static final String TOPIC_FILTER = "application/#";
    private final MqttClient client;
    private final MqttConnectOptions options;

    public MqttSubscriber(int outputCount, String serverURI, String clientId) throws MqttException {
        super(outputCount);
        this.client = new MqttClient(serverURI, clientId);
        this.options = new MqttConnectOptions();
    }

    @Override
    public void run() {
        try {
            options.setAutomaticReconnect(true);
            client.connect(options);

            // subscribe 생성
            client.subscribe(TOPIC_FILTER, (topic, msg) -> {
                // JSONObject 생성
                JSONObject object = new JSONObject(new String(msg.getPayload()));
                Message message = new JsonMessage(object);

                // Output
                output(0, message);
            });
        } catch (MqttException e) {
            log.error("Mqtt Error: {}", e.getMessage());
        }
    }
}