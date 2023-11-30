package com.nhnacademy.mqtt;

import org.json.JSONObject;

public class CommonsTopicGenerator {
    // 지사, 위치, 장치 식별 번호, 시간 정보, 센서 값이 반드시 포함해야 한다.
    private static final String DEVICE_INFO_KEY = "deviceInfo";

    public StringBuilder generate(JSONObject object) {
        StringBuilder builder = new StringBuilder();
        builder.append("data/b/" + object.getJSONObject(DEVICE_INFO_KEY).getJSONObject("tags").get("branch"))
                .append("/p/" + object.getJSONObject(DEVICE_INFO_KEY).getJSONObject("tags").get("place"))
                .append("/d/" + object.getJSONObject(DEVICE_INFO_KEY).get("devEui"))
                .append("/n/" + object.getJSONObject(DEVICE_INFO_KEY).getJSONObject("tags").get("name"))
                .append("/e/");
        return builder;
    }
}