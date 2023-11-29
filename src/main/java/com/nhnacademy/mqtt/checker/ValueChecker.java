package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;
import java.util.List;

public class ValueChecker implements Checker {
    private final List<String> valueList;

    public ValueChecker(List<String> valueList) {
        this.valueList = valueList;
    }

    @Override
    public boolean check(JSONObject jsonObject) {
        for (String value : valueList) {
            if (!containsValue(jsonObject, value)) {
                return false; // 값이 누락
            }
        }
        return true; // 모든 값이 존재
    }

    private boolean containsValue(JSONObject jsonObject, String value) {
        for (String key : jsonObject.keySet()) {
            if (jsonObject.get(key).toString().contains(value)) {
                return true; // 값이 발견
            }
        }
        return false; // 값이 발견되지 않음
    }
}
