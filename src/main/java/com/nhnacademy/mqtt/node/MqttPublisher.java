package com.nhnacademy.mqtt.node;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.nhnacademy.mqtt.message.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MqttPublisher extends InputNode {
    MqttClient client;
    MqttConnectOptions options;

    public MqttPublisher(int inputCount, MqttClient client) {
        super(inputCount);
        this.client = client;
    }

    public MqttPublisher(int inputCount, MqttClient client, MqttConnectOptions options) {
        super(inputCount);
        this.client = client;
        this.options = options;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message<JSONObject> message = tryGetMessage();
                if (options == null) {
                    client.connect();
                } else {
                    client.connect(options);
                }

                JSONObject jsonObject = message.getPayload();
                MqttMessage mqttMessage = new MqttMessage(jsonObject.toString().getBytes());
                client.publish(jsonObject.getString("topic"), mqttMessage);

                client.disconnect();
            } catch (MqttException e) {
                log.error("error message : {}", e.getMessage());
            } catch (InterruptedException e) {
                log.error("error message : {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
