package com.nhnacademy.mqtt.node;

import org.json.JSONObject;

import com.nhnacademy.mqtt.message.Message;
import com.nhnacademy.mqtt.port.Port;

public abstract class OutputNode extends Node implements Runnable {
    private final Thread thread;
    private final Port[] outputPorts;

    protected OutputNode(int outputCount) {
        thread = new Thread(this);
        outputPorts = new Port[outputCount];
    }

    public void start() {
        thread.start();
    }

    public void connect(int index, Port inputPort) {
        outputPorts[index] = inputPort;
    }

    protected void output(int index, Message<JSONObject> message) throws InterruptedException {
        outputPorts[index].put(message);
    }
}
