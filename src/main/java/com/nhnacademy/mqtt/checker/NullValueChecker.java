package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;

public class NullValueChecker implements Checker {

    @Override
    public boolean check(JSONObject jsonObject) {
        System.out.println(jsonObject.getJSONObject("payload").get("value").getClass());
        return jsonObject.getJSONObject("payload").get("value") != JSONObject.NULL;
    }
}
