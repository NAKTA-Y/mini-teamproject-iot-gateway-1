package com.nhnacademy.mqtt.message;

import org.json.JSONObject;

public class JsonMessage implements Message{
    JSONObject jsonObject;

    public JsonMessage(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public JSONObject getPayload() {
        return jsonObject;
    }
}
