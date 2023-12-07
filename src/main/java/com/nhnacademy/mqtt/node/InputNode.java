package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.port.Port;

public abstract class InputNode<T> extends Node implements Runnable {
    private final Thread thread;
    private final Port<T>[] inputPorts;

    @SuppressWarnings("unchecked")
    protected InputNode(int inputCount) {
        thread = new Thread(this);
        inputPorts = new Port[inputCount];

        for (int i = 0; i < inputCount; i++) {
            inputPorts[i] = new Port<>();
        }
    }

    public void start() {
        thread.start();
    }

    public Port<T> getInputPort(int index) {
        return inputPorts[index];
    }

    protected T tryGetMessage() throws InterruptedException {
        return inputPorts[0].get();
    }
}
