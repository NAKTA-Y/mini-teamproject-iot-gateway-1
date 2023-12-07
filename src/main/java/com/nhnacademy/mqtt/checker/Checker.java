package com.nhnacademy.mqtt.checker;

import com.nhnacademy.mqtt.message.Message;

public interface Checker {
    boolean check(Message message);
}
