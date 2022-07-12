package org.summer.net.handler.impl;

import io.netty.util.concurrent.EventExecutor;
import org.summer.database.DatabaseManager;
import org.summer.net.GameSession;
import org.summer.net.handler.PacketHandler;
import org.summer.net.packet.Packet;

public class UpdateNickNameHandler implements PacketHandler {
    @Override
    public EventExecutor getEventExecutor(GameSession session, Packet packet) {
        return session.playerExecutor();
    }

    @Override
    public void handle(GameSession session, Packet packet) {
        session.getPlayer().getPlayer().setNickname("zxxx");
        DatabaseManager.getInstance().executeWriteAsync(sqlSession -> {
            //update sql...
        });
    }

}
