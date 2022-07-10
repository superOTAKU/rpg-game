package org.summer.net.packet;

import io.netty.buffer.ByteBuf;

/**
 * 通讯数据包
 */
public class Packet {
    //头部信息
    protected PacketHeader header;
    //携带的数据
    protected byte[] payload;

    public PacketHeader getHeader() {
        return header;
    }

    public void setHeader(PacketHeader header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public void encode(ByteBuf buf) {
        buf.writeInt(header.getHeaderLen() + payload.length);
        buf.writeInt(header.getCode());
        buf.writeInt(header.getClientSequence());
        buf.writeBytes(payload);
    }

    public static Packet decode(ByteBuf buf) {
        Packet packet = new Packet();
        int len = buf.readInt();
        PacketHeader header = new PacketHeader();
        header.setCode(buf.readInt());
        header.setClientSequence(buf.readInt());
        packet.setHeader(header);
        byte[] payload = new byte[len - header.getHeaderLen()];
        buf.readBytes(payload);
        packet.setPayload(payload);
        return packet;
    }

}
