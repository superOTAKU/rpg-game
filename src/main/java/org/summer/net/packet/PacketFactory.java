package org.summer.net.packet;

import org.summer.net.dto.Result;
import org.summer.util.JacksonUtil;

/**
 * 便捷创建packet
 */
public class PacketFactory {

    public static Packet okJson(int opCode) {
        return okJson(opCode, Packet.NON_SEQUENCE, null);
    }

    public static Packet okJson(int opCode, int clientSequence) {
        return okJson(opCode, clientSequence, null);
    }

    public static Packet okJson(int opCode, int clientSequence, Object data) {
        Packet packet = new Packet();
        packet.setCode(opCode);
        packet.setClientSequence(clientSequence);
        Result result = new Result();
        result.setStatus(true);
        result.setData(data);
        packet.setPayload(JacksonUtil.toJsonBytes(result));
        return packet;
    }

    public static Packet okRspJson(Packet req) {
        return okJson(req.getCode(), req.getClientSequence());
    }

    public static Packet okRspJson(Packet req, Object data) {
        return okJson(req.getCode(), req.getClientSequence(), data);
    }

    public static Packet errJson(int opCode, int errorCode) {
        return errJson(opCode, errorCode, Packet.NON_SEQUENCE);
    }

    public static Packet errJson(int opCode, int errorCode, int clientSequence) {
        Packet packet = new Packet();
        packet.setCode(opCode);
        packet.setClientSequence(clientSequence);
        Result result = new Result();
        result.setStatus(false);
        result.setErrorCode(errorCode);
        packet.setPayload(JacksonUtil.toJsonBytes(result));
        return packet;
    }

    public static Packet errRspJson(Packet req, int errorCode) {
        return errJson(req.getCode(), errorCode, req.getClientSequence());
    }

}
