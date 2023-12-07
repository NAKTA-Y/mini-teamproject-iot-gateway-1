package com.nhnacademy.mqtt.message;

import org.json.JSONObject;

import java.net.Socket;

public class JsonWithSocketMessage extends SocketMessage{
    JSONObject jsonObject;
    public JsonWithSocketMessage(Socket socket, JSONObject jsonObject) {
        super(socket);
        this.jsonObject = jsonObject;
    }

    @Override
    public JSONObject getPayload() {
        return jsonObject;
    }
}
