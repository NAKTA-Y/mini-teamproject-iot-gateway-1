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
        List<String> targetList = new ArrayList<>();
        List<String> subKeyList = new ArrayList<>();

        // 분기 처리
        if (checkerInfo.optString("type").equals("KeyChecker")) {

            for (Object o : checkerInfo.getJSONObject(PARAMETERS).getJSONArray("keyList")) {
                targetList.add((String) o);
            }

            for (Object o : checkerInfo.getJSONObject(PARAMETERS).getJSONArray("subKeyList")) {
                subKeyList.add((String) o);
            }

            return new KeyChecker(targetList, subKeyList);

        } else if (checkerInfo.optString("type").equals("ValueChecker")) {

            for (Object o : checkerInfo.getJSONObject(PARAMETERS).getJSONArray("valueList")) {
                targetList.add((String) o);
            }

            for (Object o : checkerInfo.getJSONObject(PARAMETERS).getJSONArray("subKeyList")) {
                subKeyList.add((String) o);
            }

            String targetKey = checkerInfo.getJSONObject(PARAMETERS).optString(TARGET_KEY);

            return new ValueChecker(targetKey, targetList, subKeyList);

        }

        return null;
    }
}
