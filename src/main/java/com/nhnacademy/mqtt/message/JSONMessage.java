package com.nhnacademy.mqtt.message;

import org.json.JSONObject;

public class JSONMessage implements Message{
    JSONObject jsonObject;

    public JSONMessage(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public JSONObject getPayload() {
        return jsonObject;
    }
}
