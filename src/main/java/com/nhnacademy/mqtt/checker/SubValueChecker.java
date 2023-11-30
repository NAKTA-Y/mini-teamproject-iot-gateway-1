package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;

import java.util.List;

public class SubValueChecker extends ValueChecker {
    private final List<String> objectKeyNameList;

    public SubValueChecker(List<String> valueList, String targetKey, List<String> objectKeyNameList) {
        super(valueList, targetKey);
        this.objectKeyNameList = objectKeyNameList;
    }

    @Override
    public boolean check(JSONObject jsonObject) {
        JSONObject subJsonObject = jsonObject;
        for (String key : objectKeyNameList) {
            subJsonObject = jsonObject.getJSONObject(key);
        }
        return super.check(subJsonObject);
    }
}
