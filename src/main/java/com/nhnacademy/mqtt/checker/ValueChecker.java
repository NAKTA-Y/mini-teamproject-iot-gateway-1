package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;

import com.nhnacademy.mqtt.message.Message;

import java.util.List;

public class ValueChecker implements Checker {

    private final String targetKey;
    private final List<String> valueList;
    private final List<String> subKeyList;

    public ValueChecker(String targetKey, List<String> valueList, List<String> subKeyList) {
        this.targetKey = targetKey;
        this.valueList = valueList;
        this.subKeyList = subKeyList;
    }

    @Override
    public boolean check(Message message) {
        JSONObject jsonObject = (JSONObject) message.getPayload();

        for (String subKey : subKeyList) {
            jsonObject = jsonObject.getJSONObject(subKey);
        }

        for (String value : valueList) {
            if (jsonObject.optString(targetKey).contains(value))
                return true;
        }
        return false;
    }
}
