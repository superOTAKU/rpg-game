package org.summer.net.handler.impl;

import io.netty.util.concurrent.EventExecutor;
import org.apache.commons.lang3.StringUtils;
import org.summer.database.DatabaseManager;
import org.summer.database.entity.PlayerState;
import org.summer.database.mapper.PlayerMapper;
import org.summer.game.player.NickNameManager;
import org.summer.net.GameSession;
import org.summer.net.OpCode;
import org.summer.net.OperationCodes;
import org.summer.net.dto.SupplyPlayerInfoReq;
import org.summer.net.handler.PacketHandler;
import org.summer.net.packet.Packet;
import org.summer.util.JacksonUtil;

@OpCode(code = OperationCodes.SUPPLY_PLAYER_INFO)
public class SupplyPlayerInfoHandler implements PacketHandler {

    @Override
    public EventExecutor getEventExecutor(GameSession session, Packet packet) {
        return session.playerExecutor();
    }

    @Override
    public void handle(GameSession session, Packet packet) {
        if (session.playerCache().getPlayer().getState() != PlayerState.PENDING_INFO) {
            return;
        }
        SupplyPlayerInfoReq req = JacksonUtil.getPayload(packet, SupplyPlayerInfoReq.class);
        if (StringUtils.isBlank(req.getNickname())) {
            return;
        }
        boolean allocateResult = NickNameManager.getInstance().allocateName(session.playerId(), req.getNickname());
        if (allocateResult) {
            session.playerEntity().setNickname(req.getNickname());
            session.playerCache().setPlayerState(PlayerState.PENDING_VOCATION);
            DatabaseManager.getInstance().executeWriteAsync(sqlSession ->
                    sqlSession.getMapper(PlayerMapper.class).updateNickname(session.playerId(), req.getNickname(), PlayerState.PENDING_VOCATION));
            session.sendPacket(Packet.of(OperationCodes.SUPPLY_PLAYER_INFO));
        }
    }

}
