package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.message.Message;
import com.nhnacademy.mqtt.port.Port;

public abstract class InputNode extends Node implements Runnable {
    private final Thread thread;
    private final Port[] inputPorts;

    protected InputNode(int inputCount) {
        thread = new Thread(this);
        inputPorts = new Port[inputCount];

        for (int i = 0; i < inputCount; i++) {
            inputPorts[i] = new Port();
        }
    }

    public void start() {
        thread.start();
    }

    public Port getInputPort(int index) {
        return inputPorts[index];
    }

    public int getInputPortCount() {
        return inputPorts.length;
    }

    protected Message tryGetMessage() throws InterruptedException {
        return inputPorts[0].get();
    }
}
