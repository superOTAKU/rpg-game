package org.summer.net.packet;

import io.netty.buffer.ByteBuf;

/**
 * 通讯数据包
 */
public class Packet {
    public static final int NON_SEQUENCE = -1;

    //请求码
    private int code;
    //客户端请求顺序标识
    private int clientSequence = NON_SEQUENCE;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getClientSequence() {
        return clientSequence;
    }

    public void setClientSequence(int clientSequence) {
        this.clientSequence = clientSequence;
    }

    //携带的数据
    protected byte[] payload;

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public void encode(ByteBuf buf) {
        buf.writeInt(8 + payload.length);
        buf.writeInt(code);
        buf.writeInt(clientSequence);
        buf.writeBytes(payload);
    }

    public static Packet decode(ByteBuf buf) {
        Packet packet = new Packet();
        int len = buf.readInt();
        packet.setCode(buf.readInt());
        packet.setClientSequence(buf.readInt());
        byte[] payload = new byte[len - 8];
        buf.readBytes(payload);
        packet.setPayload(payload);
        return packet;
    }

    public static Packet of(int code) {
        Packet packet = new Packet();
        packet.setCode(1);
        return packet;
    }

}
