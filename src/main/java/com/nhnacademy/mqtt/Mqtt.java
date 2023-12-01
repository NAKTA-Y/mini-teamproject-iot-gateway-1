package com.nhnacademy.mqtt;

import com.nhnacademy.mqtt.checker.KeyChecker;
import com.nhnacademy.mqtt.checker.NotNullValueChecker;
import com.nhnacademy.mqtt.checker.SubValueChecker;
import com.nhnacademy.mqtt.checker.ValueChecker;
import com.nhnacademy.mqtt.node.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class Mqtt {
    private static final String PROPERTIES_PATH = "src/main/java/com/nhnacademy/mqtt/properties.json";
    private static final String SETTING_PATH = "src/main/java/com/nhnacademy/mqtt/setting.json";
    private static final String SERVER_URI_KEY_NAME = "serverURI";
    private static final String KEY_FILTER = "KeyFilter";
    private static final String KEY_LIST = "keyList";
    private static final String TARGET_KEY = "targetKey";
    private static final String VALUE_LIST = "valueList";
    private static final String MQTT_SUBSCRIBER = "MqttSubscriber";
    private static final String APP_NAME_FILTER = "AppNameFilter";
    private static final String NULL_VALUE_FILTER = "NullValueFilter";
    private static final String SENSER_TYPE_FILTER = "SensorTypeFilter";
    private static final String OBJECT_KEY_NAME_LIST = "objectKeyNameList";
    private static final String PARAMETERS = "parameters";
    private static final String CHECKER = "Checker";

    private static JSONObject jsonObject;

    public static void main(String[] args) throws JSONException {
        jsonObject = jsonFileReader(PROPERTIES_PATH);
        setCommentLineArgument(args);
        try {
            connect();
        } catch (JSONException e) {
            log.error("properties 파일 속성 읽기 실패");
        } catch (MqttException e) {
            log.error("Mqtt Error: {}", e.getMessage());
        }
    }

    private static void connect() throws JSONException, MqttException {
        MqttSubscriber mqttSubscriber = new MqttSubscriber(1,
                getValue(jsonObject.getJSONObject(MQTT_SUBSCRIBER), SERVER_URI_KEY_NAME),
                UUID.randomUUID().toString());

        Filter keyFilter = new Filter(1, 1,
                new KeyChecker(getValueList(jsonObject.getJSONObject(KEY_FILTER).getJSONArray(KEY_LIST))));

        Filter appNameFilter = new Filter(1, 1,
                new SubValueChecker(getValueList(jsonObject.getJSONObject(APP_NAME_FILTER).getJSONArray(VALUE_LIST)),
                        getValue(jsonObject.getJSONObject(APP_NAME_FILTER), TARGET_KEY),
                        getValueList(jsonObject.getJSONObject(APP_NAME_FILTER).getJSONArray(OBJECT_KEY_NAME_LIST))));

        ObjectGenerator objectGenerator = new ObjectGenerator(1, 1);

        Filter nullValueFilter = new Filter(1, 1,
                new NotNullValueChecker(getValue(jsonObject.getJSONObject(NULL_VALUE_FILTER), TARGET_KEY)));

        Filter sensorTypeFilter = new Filter(1, 1,
                new ValueChecker(getValueList(jsonObject.getJSONObject(SENSER_TYPE_FILTER).getJSONArray(VALUE_LIST)),
                        getValue(jsonObject.getJSONObject(SENSER_TYPE_FILTER), TARGET_KEY)));

        MqttPublisher mqttPublisher = new MqttPublisher(1,
                getValue(jsonObject.getJSONObject("MqttPublisher"), SERVER_URI_KEY_NAME), UUID.randomUUID().toString());

        mqttSubscriber.connect(0, keyFilter.getInputPort(0));
        keyFilter.connect(0, appNameFilter.getInputPort(0));
        appNameFilter.connect(0, objectGenerator.getInputPort(0));
        objectGenerator.connect(0, nullValueFilter.getInputPort(0));
        nullValueFilter.connect(0, sensorTypeFilter.getInputPort(0));
        sensorTypeFilter.connect(0, mqttPublisher.getInputPort(0));

        mqttSubscriber.start();
        keyFilter.start();
        appNameFilter.start();
        objectGenerator.start();
        nullValueFilter.start();
        sensorTypeFilter.start();
        mqttPublisher.start();

    }

    // command line arguments
    public static void setCommentLineArgument(String[] args) {
        Options options = new Options();
        options.addOption("an", "an", true, "filter app name");
        options.addOption("s", true, "filter sensor type");
        options.addOption("c", false, "use json.setting");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            hasOption(cmd);
        } catch (org.apache.commons.cli.ParseException e) {
            log.error("parser Error: {}", e.getMessage());
        }

    }

    // 옵션 처리
    // 위에 디폴트 JSONObject 객체 만들기
    // 1. c가 있는가?
    // 있으면 -> 설정파일 읽어서 JSONobject 값 바꾸기
    // 없으면 -> 그대로 사용
    // 2. an이나 s가 있으면 JSOONObject 값 바꾸기
    // drop으로 원래 값 지우고 put으로 삽입? or 키에 대한 value값 수정
    // 3. JSONObject 사용해서 Factory로 Checker 생성
    // 4. 만들어진 Checker를 사용해서 Node 생성 후 Connect

    // 1. 그대로
    // 2. JSONObect 사용해서 Checker 만들기
    // 3. argument가 있으면 Checker에 setter 만들어서 List 값 바꾸기
    // 4. Connect

    private static void hasOption(CommandLine cmd) {
        if (cmd.hasOption("c")) {
            JSONObject settingJson = jsonFileReader(SETTING_PATH);
            setting(settingJson);
        }

        if (cmd.hasOption("an") && cmd.getOptionValue("an") != null) {
            String[] anSplit = cmd.getOptionValue("an").split(",");

            jsonObject.getJSONObject(APP_NAME_FILTER).put(VALUE_LIST, new JSONArray(Arrays.asList(anSplit)));
        }
        if (cmd.hasOption("s") && cmd.getOptionValue("s") != null) {
            String[] sSplit = cmd.getOptionValue("s").split(",");

            jsonObject.getJSONObject(SENSER_TYPE_FILTER).put(VALUE_LIST, new JSONArray(Arrays.asList(sSplit)));
        }
    }

    // JSON 파일 읽기
    public static JSONObject jsonFileReader(String filePath) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONParser parser = new JSONParser();
            Reader reader = new FileReader(filePath);
            jsonObject = new JSONObject(parser.parse(reader).toString());

        } catch (JSONException e) {
            log.error("JSON Error: {}", e.getMessage());
        } catch (FileNotFoundException e) {
            log.error("File Error: {}", e.getMessage());
        } catch (IOException e) {
            log.error("IO Error: {}", e.getMessage());
        } catch (ParseException e) {
            log.error("parser Error: {}", e.getMessage());
        }
        return jsonObject;
    }

    private static void setting(JSONObject settingJson) {
        JSONArray keyFilterKeyList = settingJson.getJSONObject(KEY_FILTER)
                .getJSONObject(PARAMETERS)
                .getJSONObject(CHECKER)
                .getJSONObject(PARAMETERS).getJSONArray(KEY_LIST);
        JSONArray appNameFilterValueList = settingJson.getJSONObject(APP_NAME_FILTER)
                .getJSONObject(PARAMETERS)
                .getJSONObject(CHECKER)
                .getJSONObject(PARAMETERS)
                .getJSONArray(VALUE_LIST);
        JSONArray appNameFilterObjectKeyNameList = settingJson.getJSONObject(APP_NAME_FILTER)
                .getJSONObject(PARAMETERS)
                .getJSONObject(CHECKER)
                .getJSONObject(PARAMETERS)
                .getJSONArray(OBJECT_KEY_NAME_LIST);
        JSONArray sensorTypeFilterValueList = settingJson.getJSONObject(SENSER_TYPE_FILTER)
                .getJSONObject(PARAMETERS)
                .getJSONObject(CHECKER)
                .getJSONObject(PARAMETERS)
                .getJSONArray(VALUE_LIST);

        String mqttInServerURI = settingJson.getJSONObject("mqttIn")
                .getJSONObject(PARAMETERS)
                .getString(SERVER_URI_KEY_NAME);
        String appNameFilterTargetKey = settingJson.getJSONObject(APP_NAME_FILTER)
                .getJSONObject(PARAMETERS)
                .getJSONObject(CHECKER)
                .getJSONObject(PARAMETERS)
                .getString(TARGET_KEY);
        String nullValueFilterTargetKey = settingJson.getJSONObject(
                NULL_VALUE_FILTER)
                .getJSONObject(PARAMETERS)
                .getJSONObject(CHECKER)
                .getJSONObject(PARAMETERS)
                .getString(TARGET_KEY);
        String sensorTypeFilterTargetKey = settingJson.getJSONObject(SENSER_TYPE_FILTER)
                .getJSONObject(PARAMETERS)
                .getJSONObject(CHECKER)
                .getJSONObject(PARAMETERS)
                .getString(TARGET_KEY);
        String mqttOutServerURI = settingJson.getJSONObject("mqttOut")
                .getJSONObject(PARAMETERS)
                .getString(SERVER_URI_KEY_NAME);

        jsonObject.getJSONObject(MQTT_SUBSCRIBER).put(SERVER_URI_KEY_NAME, mqttInServerURI);
        jsonObject.getJSONObject(KEY_FILTER).put(KEY_LIST, keyFilterKeyList);
        jsonObject.getJSONObject(APP_NAME_FILTER).put(VALUE_LIST, appNameFilterValueList);
        jsonObject.getJSONObject(APP_NAME_FILTER).put(TARGET_KEY, appNameFilterTargetKey);
        jsonObject.getJSONObject(APP_NAME_FILTER).put(OBJECT_KEY_NAME_LIST, appNameFilterObjectKeyNameList);
        jsonObject.getJSONObject(NULL_VALUE_FILTER).put(TARGET_KEY, nullValueFilterTargetKey);
        jsonObject.getJSONObject(SENSER_TYPE_FILTER).put(VALUE_LIST, sensorTypeFilterValueList);
        jsonObject.getJSONObject(SENSER_TYPE_FILTER).put(TARGET_KEY, sensorTypeFilterTargetKey);
        jsonObject.getJSONObject("MqttPublisher").put(SERVER_URI_KEY_NAME, mqttOutServerURI);
    }

    private static List<String> getValueList(JSONArray jsonArray) {
        List<String> resultList = new ArrayList<>();

        for (Object element : jsonArray) {
            resultList.add((String) element);
        }

        return resultList;
    }

    private static String getValue(JSONObject jsonObject, String key) {
        return jsonObject.optString(key);
    }
}
