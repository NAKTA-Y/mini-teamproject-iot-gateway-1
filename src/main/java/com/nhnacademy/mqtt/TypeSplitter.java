package com.nhnacademy.mqtt;

import java.util.Map;

import org.json.JSONObject;

public class TypeSplitter {
    
    public Map<String, Object> generate(JSONObject object){
        return object.getJSONObject("object").toMap();
    }

}
