package org.summer.net.dto;

import org.summer.game.player.PlayerCache;
import org.summer.net.OperationCodes;
import org.summer.net.packet.Packet;
import org.summer.util.JacksonUtil;

public class LoginRspPacket extends Packet {

    public LoginRspPacket(PlayerCache player) {
        setCode(OperationCodes.LOGIN);
        LoginRsp rsp = new LoginRsp();
        rsp.setPlayerId(player.getPlayerId());
        rsp.setState(player.getSession().getState());
        //TODO 从cache收集客户端需要的数据
        rsp.setPlayerData(null);
        //使用JSON作为传输协议
        setPayload(JacksonUtil.toJsonBytes(rsp));
    }

}
