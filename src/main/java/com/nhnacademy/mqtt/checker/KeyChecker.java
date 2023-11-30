package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;

import java.util.List;

public class KeyChecker implements Checker {
    private final List<String> keyList;

    public KeyChecker(List<String> keyList) {
        this.keyList = keyList;
    }

    @Override
    public boolean check(JSONObject jsonObject) {
        for (String key : keyList) {
            if (jsonObject.has(key)) {
                return true;
            }
        }
        return false;
    }
}
