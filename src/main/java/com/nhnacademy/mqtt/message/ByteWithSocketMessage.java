package com.nhnacademy.mqtt.message;

import java.net.Socket;

public class ByteWithSocketMessage extends SocketMessage{
    private byte[] bytes;
    protected ByteWithSocketMessage(Socket socket, byte[] bytes) {
        super(socket);
        this.bytes  = bytes;
    }

    @Override
    public byte[] getPayload() {
        return bytes;
    }
}
