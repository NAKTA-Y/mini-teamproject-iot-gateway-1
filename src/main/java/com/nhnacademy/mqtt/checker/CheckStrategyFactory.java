package com.nhnacademy.mqtt.checker;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckStrategyFactory {

    private CheckStrategyFactory() {
        log.error("유틸리티 클래스 생성 시도");
        throw new IllegalStateException();
    }

    public static CheckStrategy<JSONObject> create(String type, String filter, JSONObject checkerInfo) {
        List<String> targetList = new ArrayList<>();
        List<String> subKeyList = new ArrayList<>();

        // 분기 처리
        if (type.equals("KeyChecker")) {

            for (Object o : checkerInfo.getJSONObject(filter).getJSONArray("keyList")) {
                targetList.add((String) o);
            }

            for (Object o : checkerInfo.getJSONObject(filter).getJSONArray("subKeyList")) {
                subKeyList.add((String) o);
            }

            return new JSONKeyStrategy(targetList, subKeyList);

        } else if (type.equals("ValueChecker")) {

            for (Object o : checkerInfo.getJSONObject(filter).getJSONArray("valueList")) {
                targetList.add((String) o);
            }

            for (Object o : checkerInfo.getJSONObject(filter).getJSONArray("subKeyList")) {
                subKeyList.add((String) o);
            }

            String targetKey = checkerInfo.getJSONObject(filter).optString("targetKey");

            return new JSONValueStrategy(targetKey, targetList, subKeyList);

        }

        return null;
    }
}
