package com.nhnacademy.mqtt.checker;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Checker<T> {
    private final CheckStrategy<T> checkStrategy;

    public Checker(CheckStrategy<T> checkStrategy) {
        this.checkStrategy = checkStrategy;
    }

    @SuppressWarnings("unchecked")
    public boolean check(Object data) {
        try {
            return checkStrategy.check((T) data);
        } catch (ClassCastException e) {
            log.error("Type Casting Exception 발생: {}", e.getMessage());
            return false;
        }
    }
}
