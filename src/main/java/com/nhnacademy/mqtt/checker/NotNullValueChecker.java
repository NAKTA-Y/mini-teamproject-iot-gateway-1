package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;

public class NotNullValueChecker implements Checker {
    private final String targetKey;

    public NotNullValueChecker(String targetKey) {
        this.targetKey = targetKey;
    }

    @Override
    public boolean check(JSONObject jsonObject) {
        return jsonObject.get(targetKey) != JSONObject.NULL;
    }
}
