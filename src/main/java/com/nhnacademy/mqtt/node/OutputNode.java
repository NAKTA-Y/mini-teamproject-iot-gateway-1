package com.nhnacademy.mqtt.node;


import com.nhnacademy.mqtt.message.Message;
import com.nhnacademy.mqtt.port.Port;

public abstract class OutputNode extends ActiveNode{
    private final Port[] outputPorts;

    protected OutputNode(int outputCount) {
        outputPorts = new Port[outputCount];
    }

    public void connect(int index, Port inputPort) {
        outputPorts[index] = inputPort;
    }

    protected void output(int index, Message message) throws InterruptedException{
            outputPorts[index].put(message);
    }
}
