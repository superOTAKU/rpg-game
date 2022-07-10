package org.summer.net.packet;

public class PacketHeader {
    private static final int NON_SEQUENCE = -1;

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

    public int getHeaderLen() {
        return 8;
    }

}
