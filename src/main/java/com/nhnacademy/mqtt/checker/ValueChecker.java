package com.nhnacademy.mqtt.checker;

import org.json.JSONObject;
import java.util.List;

public class ValueChecker implements Checker {

    private final List<String> valueList;
    private final String targetKey;

    public ValueChecker(List<String> valueList, String targetKey) {
        this.valueList = valueList;
        this.targetKey = targetKey;
    }

    // appname, nullvalue, sensortype 의 공통 값은 아래와 같다.
    @Override
    public boolean check(JSONObject jsonObject) {
        for (String value : valueList) {
            if (jsonObject.getString(targetKey).contains(value))
                return true; 
        }
        return false; 
    }
}
