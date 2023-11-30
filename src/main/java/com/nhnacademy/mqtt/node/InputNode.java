package com.nhnacademy.mqtt.node;


import com.nhnacademy.mqtt.message.Message;
import com.nhnacademy.mqtt.port.Port;

public abstract class InputNode extends ActiveNode{
    private final Port[] inputPorts;

    protected InputNode(int inputCount) {
        inputPorts = new Port[inputCount];

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

    protected Message tryGetMessage() throws InterruptedException{
            return inputPorts[0].get();
    }
}
