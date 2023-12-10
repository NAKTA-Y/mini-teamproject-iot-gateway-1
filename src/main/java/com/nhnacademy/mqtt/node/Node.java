package com.nhnacademy.mqtt.node;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public abstract class Node {
    private static final AtomicInteger COUNT = new AtomicInteger(0);
    private final String id;

    protected Node() {
        id = String.format("%s-%02d", getClass().getSimpleName(), COUNT.incrementAndGet());
    }
}
