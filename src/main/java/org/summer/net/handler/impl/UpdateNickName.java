package org.summer.net.handler.impl;

import io.netty.util.concurrent.EventExecutor;
import org.summer.database.DatabaseManager;
import org.summer.game.Executors;
import org.summer.net.GameSession;
import org.summer.net.handler.PacketHandler;
import org.summer.net.packet.Packet;

public class UpdateNickName implements PacketHandler {
    @Override
    public EventExecutor getEventExecutor(GameSession session, Packet packet) {
        return Executors.getInstance().getPlayerExecutor(session.getPlayer().getPlayerId());
    }

    @Override
    public void handle(GameSession session, Packet packet) {
        String nickName = session.getPlayer().getPlayer().getNickname();
        session.getPlayer().getPlayer().setNickname("zxxx");
        DatabaseManager.getInstance().executeWriteAsync(sqlSession -> {
            //update sql...
        });
    }

}
