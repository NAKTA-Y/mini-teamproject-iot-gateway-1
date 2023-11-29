package com.nhnacademy.mqtt.message;

public class Message<T> {
    private final T payload;

    public Message(T paylaod) {
        this.payload = paylaod;
    }

    public T getPayload() {
        return payload;
    }
}
