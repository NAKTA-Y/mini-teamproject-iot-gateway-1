package com.nhnacademy.mqtt.node;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public abstract class Node {
    private static AtomicInteger count = new AtomicInteger(0);
    private final String id;

    protected Node() {
        id = String.format("%s-%02d", getClass().getSimpleName(), count.incrementAndGet());
    }
}
