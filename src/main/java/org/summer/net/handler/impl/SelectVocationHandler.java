package org.summer.net.handler.impl;

import io.netty.util.concurrent.EventExecutor;
import org.summer.database.DatabaseManager;
import org.summer.database.entity.PlayerState;
import org.summer.database.mapper.PlayerMapper;
import org.summer.net.GameSession;
import org.summer.net.OpCode;
import org.summer.net.OperationCodes;
import org.summer.net.dto.SelectVocationReq;
import org.summer.net.handler.PacketHandler;
import org.summer.net.packet.Packet;
import org.summer.net.packet.PacketFactory;
import org.summer.util.JacksonUtil;

@OpCode(code = OperationCodes.SELECT_VOCATION)
public class SelectVocationHandler implements PacketHandler {
    @Override
    public EventExecutor getEventExecutor(GameSession session, Packet packet) {
        return session.playerExecutor();
    }

    @Override
    public void handle(GameSession session, Packet packet) {
        SelectVocationReq req = JacksonUtil.getPayload(packet, SelectVocationReq.class);
        session.playerEntity().setVocation(req.getVocation());
        session.playerCache().setPlayerState(PlayerState.NORMAL);
        DatabaseManager.getInstance().executeWriteAsync(sqlSession -> {
            sqlSession.getMapper(PlayerMapper.class).updateVocation(session.playerId(), req.getVocation(), PlayerState.NORMAL);
        });
        session.sendPacket(PacketFactory.okJson(OperationCodes.SELECT_VOCATION));
    }

}
