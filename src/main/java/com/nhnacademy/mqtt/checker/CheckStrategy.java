package com.nhnacademy.mqtt.checker;

public interface CheckStrategy<T> {
    boolean check(T data);
}
