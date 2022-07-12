package org.summer.net.dto;

import org.summer.net.GameSession;
import org.summer.net.OperationCodes;
import org.summer.net.packet.Packet;
import org.summer.util.JacksonUtil;

public class LoginRspPacket extends Packet {

    public LoginRspPacket(Long playerId, GameSession.SessionState state, int clientSequence) {
        setCode(OperationCodes.LOGIN);
        setClientSequence(clientSequence);
        LoginRsp rsp = new LoginRsp();
        rsp.setPlayerId(playerId);
        rsp.setState(state);
        setPayload(JacksonUtil.toJsonBytes(rsp));
    }

}
