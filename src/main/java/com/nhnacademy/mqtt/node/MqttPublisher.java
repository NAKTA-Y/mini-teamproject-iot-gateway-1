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
    private MqttClient client;
    MqttConnectOptions options;

    public MqttPublisher(int inputCount, String serverURI, String clientId) throws MqttException {
        super(inputCount);
        this.client = new MqttClient(serverURI, clientId);
        this.options = new MqttConnectOptions();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message<JSONObject> message = tryGetMessage();
                options.setAutomaticReconnect(true);
                client.connect();

                JSONObject jsonObject = message.getPayload();
                log.info("{}", jsonObject);
                MqttMessage mqttMessage = new MqttMessage(jsonObject.toString().getBytes());
                client.publish(jsonObject.getString("topic"), mqttMessage);

                client.disconnect();
            } catch (MqttException e) {
                log.error("Mqtt Error : {}", e.getMessage());
            } catch (InterruptedException e) {
                log.error("Thread Error : {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }
}
