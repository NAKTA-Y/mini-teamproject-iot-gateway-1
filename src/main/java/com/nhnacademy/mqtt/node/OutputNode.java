package com.nhnacademy.mqtt.node;

import com.nhnacademy.mqtt.port.Port;

public abstract class OutputNode<T> extends Node implements Runnable {
    private final Thread thread;
    private final Port<T>[] outputPorts;

    @SuppressWarnings("unchecked")
    protected OutputNode(int outputCount) {
        thread = new Thread(this);
        outputPorts = new Port[outputCount];
    }

    public void start() {
        thread.start();
    }

    public void connect(int index, Port<T> inputPort) {
        outputPorts[index] = inputPort;
    }

    protected void output(int index, T message) throws InterruptedException {
        outputPorts[index].put(message);
    }
}
