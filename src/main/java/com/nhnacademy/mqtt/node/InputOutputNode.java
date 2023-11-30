package com.nhnacademy.mqtt.node;

import org.json.JSONObject;

import com.nhnacademy.mqtt.message.Message;
import com.nhnacademy.mqtt.port.Port;

public abstract class InputOutputNode extends ActiveNode {
    private final Port[] inputPorts;
    private final Port[] outputPorts;

    protected InputOutputNode(int inputCount, int outputCount) {
        inputPorts = new Port[inputCount];
        outputPorts = new Port[outputCount];

        for (int i = 0; i < inputCount; i++) {
            inputPorts[i] = new Port();
        }
    }

    public Port getInputPort(int index) {
        return inputPorts[index];
    }

    public int getInputPortCount() {
        return inputPorts.length;
    }

    public void connect(int index, Port inputPort) {
        outputPorts[index] = inputPort;
    }

    protected void output(int index, Message<JSONObject> message) throws InterruptedException {
        outputPorts[index].put(message);
    }

    protected Message<JSONObject> tryGetMessage() throws InterruptedException {
        return inputPorts[0].get();
    }
}
