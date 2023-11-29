package com.nhnacademy.mqtt;

import org.json.JSONObject;

public class CommonsTopicGenerator{
    // 지사, 위치, 장치 식별 번호, 시간 정보, 센서 값이 반드시 포함해야 한다.

    public StringBuilder generate(JSONObject object) {
        StringBuilder builder = new StringBuilder();
        builder.append("data/b/" + object.getJSONObject("deviceInfo").getJSONObject("tags").get("branch"))
                .append("/p/" + object.getJSONObject("deviceInfo").getJSONObject("tags").get("place"))
                .append("/n/" + object.getJSONObject("deviceInfo").getJSONObject("tags").get("name"))
                .append("/d/" + object.getJSONObject("deviceInfo").get("devEui"))
                .append("/e/");
        return builder;
    }
}