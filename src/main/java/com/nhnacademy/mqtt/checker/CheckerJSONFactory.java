package com.nhnacademy.mqtt.checker;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckerJSONFactory {
    private static final String TARGET_KEY = "targetKey";
    private static final String PARAMETERS = "parameters";

    private CheckerJSONFactory() {
        log.error("유틸리티 클래스 생성 시도");
        throw new IllegalStateException();
    }

    public static Checker create(JSONObject checkerInfo) {
        // 분기 처리
        if (checkerInfo.optString("type").equals("KeyChecker")) {
            List<String> keyList = new ArrayList<>();

            for (Object o : checkerInfo.getJSONObject(PARAMETERS).getJSONArray("keyList")) {
                keyList.add((String) o);
            }

            return new KeyChecker(keyList);

        } else if (checkerInfo.optString("type").equals("SubValueChecker")) {
            List<String> valueList = new ArrayList<>();
            List<String> objectKeyNameList = new ArrayList<>();
            String targetKey = checkerInfo.optString(TARGET_KEY);

            for (Object o : checkerInfo.getJSONObject(PARAMETERS).getJSONArray("valueList")) {
                valueList.add((String) o);
            }

            for (Object o : checkerInfo.getJSONObject(PARAMETERS).getJSONArray("objectKeyNameList")) {
                objectKeyNameList.add((String) o);
            }

            return new SubValueChecker(valueList, targetKey, objectKeyNameList);

        } else if (checkerInfo.optString("type").equals("NotNullValueChecker")) {
            return new NotNullValueChecker(checkerInfo.getJSONObject(PARAMETERS).optString(TARGET_KEY));

        } else if (checkerInfo.optString("type").equals("ValueChecker")) {
            List<String> valueList = new ArrayList<>();
            for (Object o : checkerInfo.getJSONObject(PARAMETERS).getJSONArray("valueList")) {
                valueList.add((String) o);
            }
            String targetKey = checkerInfo.getJSONObject(PARAMETERS).optString(TARGET_KEY);

            return new ValueChecker(valueList, targetKey);

        }
        return null;
    }
}
