package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.port.Port;

public abstract class InputOutputNode<T, G> extends Node implements Runnable {
    private final Thread thread;
    private final Port<T>[] inputPorts;
    private final Port<G>[] outputPorts;

    @SuppressWarnings("unchecked")
    protected InputOutputNode(int inputCount, int outputCount) {
        thread = new Thread(this);
        inputPorts = new Port[inputCount];
        outputPorts = new Port[outputCount];

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

    public void connect(int index, Port<G> inputPort) {
        outputPorts[index] = inputPort;
    }

    protected void output(int index, G message) throws InterruptedException {
        outputPorts[index].put(message);
    }

    protected T tryGetMessage() throws InterruptedException {
        return inputPorts[0].get();
    }
}
