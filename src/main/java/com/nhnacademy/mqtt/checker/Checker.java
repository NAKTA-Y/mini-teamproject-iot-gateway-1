package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;

public interface Checker {
    boolean check(JSONObject jsonObject);
}
