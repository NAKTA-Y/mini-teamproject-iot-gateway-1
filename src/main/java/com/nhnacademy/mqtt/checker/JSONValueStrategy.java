package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;

import java.util.List;

public class JSONValueStrategy implements CheckStrategy<JSONObject> {

    private final String targetKey;
    private final List<String> valueList;
    private final List<String> subKeyList;

    public JSONValueStrategy(String targetKey, List<String> valueList, List<String> subKeyList) {
        this.targetKey = targetKey;
        this.valueList = valueList;
        this.subKeyList = subKeyList;
    }

    @Override
    public boolean check(JSONObject object) {

        for (String subKey : subKeyList) {
            object = object.getJSONObject(subKey);
        }

        for (String value : valueList) {
            if (object.optString(targetKey).contains(value))
                return true;
        }

        return false;
    }
}
