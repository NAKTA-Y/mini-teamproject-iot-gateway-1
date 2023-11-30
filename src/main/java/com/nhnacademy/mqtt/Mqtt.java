package com.nhnacademy.mqtt;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import com.nhnacademy.mqtt.checker.KeyChecker;
import com.nhnacademy.mqtt.checker.NullValueChecker;
import com.nhnacademy.mqtt.checker.ValueChecker;
import com.nhnacademy.mqtt.node.AppNameFilter;
import com.nhnacademy.mqtt.node.KeyFilter;
import com.nhnacademy.mqtt.node.MqttPublisher;
import com.nhnacademy.mqtt.node.MqttSubscriber;
import com.nhnacademy.mqtt.node.NullValueFilter;
import com.nhnacademy.mqtt.node.ObjectGenerator;
import com.nhnacademy.mqtt.node.SensorTypeFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * 1. 디폴트값 설정
 * 2. 세팅파일이 있으면 세팅파일에서 가져오기
 * 3. --an이나 -s가 있으면 그거 적용
 *
 */
@Slf4j
public class Mqtt {
    private static final String PUBLISH_ID = "test";
    static final String APP_TARGET_KEY = "applicationName";
    static final String SENSOR_TARGET_KEY = "topic";

    static String appName = "NHNAcademyEMS";
    static String sensorType = "humidity,temperature";
    static List<String> keyList = Arrays.asList("deviceInfo");
    static List<String> appNameList;
    static List<String> sensorTypeList;

    public static void main(String[] args) {
        setCommentLineArgument(args);

        try {
            MqttClient subClient = new MqttClient("tcp://ems.nhnacademy.com", PUBLISH_ID);
            MqttClient pubClient = new MqttClient("tcp://localhost:1883", PUBLISH_ID);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);

            connect(subClient, pubClient, options);
        } catch (MqttException e) {
            log.error("{}", e.getMessage());
        }
    }

    private static void connect(MqttClient subClient, MqttClient pubClient, MqttConnectOptions options) {
        MqttSubscriber mqttSubscribe = new MqttSubscriber(1, subClient, options);
        KeyFilter keyFilter = new KeyFilter(1, 1, new KeyChecker(keyList));
        AppNameFilter appNameFilter = new AppNameFilter(1, 1, new ValueChecker(appNameList, APP_TARGET_KEY));
        ObjectGenerator objectGenerator = new ObjectGenerator(1, 1);
        NullValueFilter nullValueFilter = new NullValueFilter(1, 1, new NullValueChecker());
        SensorTypeFilter sensorTypeFilter = new SensorTypeFilter(1, 1,
                new ValueChecker(sensorTypeList, SENSOR_TARGET_KEY));
        MqttPublisher mqttPublisher = new MqttPublisher(1, pubClient);

        mqttSubscribe.connect(0, keyFilter.getInputPort(0));
        keyFilter.connect(0, appNameFilter.getInputPort(0));
        appNameFilter.connect(0, objectGenerator.getInputPort(0));
        objectGenerator.connect(0, nullValueFilter.getInputPort(0));
        nullValueFilter.connect(0, sensorTypeFilter.getInputPort(0));
        sensorTypeFilter.connect(0, mqttPublisher.getInputPort(0));

        mqttSubscribe.start();
        keyFilter.start();
        appNameFilter.start();
        objectGenerator.start();
        nullValueFilter.start();
        sensorTypeFilter.start();
        mqttPublisher.start();
    }

    // command line argument
    public static void setCommentLineArgument(String[] args) {
        Options options = new Options();
        options.addOption("an", "an", true, "filter app name");
        options.addOption("s", true, "filter sensor type");
        options.addOption("c", false, "use json.setting");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            hasOption(cmd);
        } catch (ParseException e) {
            log.error("{}", e.getMessage());
        }

    }

    // 옵션 처리
    private static void hasOption(CommandLine cmd) {
        if (cmd.hasOption("c")) {
            jsonFileReader();
        }
        if (cmd.hasOption("an") && cmd.getOptionValue("an") != null) {
            appName = cmd.getOptionValue("an");
        }
        if (cmd.hasOption("s") && cmd.getOptionValue("s") != null) {
            sensorType = cmd.getOptionValue("s");
        }
        appNameList = Arrays.asList(appName.split(","));
        sensorTypeList = Arrays.asList(sensorType.split(","));
    }

    // JSON 파일 읽기
    public static void jsonFileReader() {
        try {
            JSONParser parser = new JSONParser();
            Reader reader = new FileReader("src/main/java/com/nhnacademy/mqtt/setting.json");
            JSONObject jsonObject = new JSONObject(parser.parse(reader).toString());

            appName = (String) jsonObject.get("appName");
            sensorType = (String) jsonObject.get("snesorType");

        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }
}
