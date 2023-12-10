package com.nhnacademy.mqtt.message;

import java.net.Socket;

public abstract class SocketMessage implements Message{
    private final Socket socket;

    protected SocketMessage(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
