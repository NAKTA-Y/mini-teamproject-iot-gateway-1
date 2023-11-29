package com.nhnacademy.mqtt.node;

public abstract class ActiveNode extends Node implements Runnable{
    private final Thread thread;

    protected ActiveNode() {
        thread = new Thread(this);
    }

    public void start() {
        thread.start();
    }
}
