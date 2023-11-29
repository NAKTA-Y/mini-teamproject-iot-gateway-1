package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;

public class NullValueChecker implements Checker {

    @Override
    public boolean check(JSONObject jsonObject) {
        return jsonObject.getJSONObject("payload").get("value") != null;
    }
}
