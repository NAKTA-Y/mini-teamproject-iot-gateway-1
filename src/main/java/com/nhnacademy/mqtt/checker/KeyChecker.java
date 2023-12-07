package com.nhnacademy.mqtt.checker;

import com.nhnacademy.mqtt.message.Message;
import org.json.JSONObject;

import java.util.List;

public class KeyChecker implements Checker {
    private final List<String> targetKeyList;
    private final List<String> subKeyList;

    public KeyChecker(List<String> targetKeyList, List<String> subKeyList) {
        this.targetKeyList = targetKeyList;
        this.subKeyList = subKeyList;
    }

    @Override
    public boolean check(Message message) {
        JSONObject object = (JSONObject) message.getPayload();

        for (String subKey : subKeyList) {
            object = object.getJSONObject(subKey);
        }

        for (String key : targetKeyList) {
            if (object.has(key)) {
                return true;
            }
        }
        return false;
    }
}
