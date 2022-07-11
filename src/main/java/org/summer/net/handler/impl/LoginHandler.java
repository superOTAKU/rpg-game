package org.summer.net.handler.impl;

import io.netty.util.concurrent.EventExecutor;
import org.summer.database.DatabaseManager;
import org.summer.database.entity.Player;
import org.summer.game.Executors;
import org.summer.game.player.PlayerCache;
import org.summer.game.player.PlayerCacheManager;
import org.summer.net.GameSession;
import org.summer.net.OpCode;
import org.summer.net.OperationCodes;
import org.summer.net.dto.LoginReq;
import org.summer.net.handler.PacketHandler;
import org.summer.net.packet.Packet;

@OpCode(code = OperationCodes.LOGIN)
public class LoginHandler implements PacketHandler {

    @Override
    public EventExecutor getEventExecutor(GameSession session, Packet packet) {
        return Executors.getInstance().getLoginExecutor();
    }

    @Override
    public void handle(GameSession session, Packet packet) {
        LoginReq req = LoginReq.parse(packet);
        //TODO 校验token
        String accountId = null;
        PlayerCache player = PlayerCacheManager.getInstance().getByAccountId(accountId);
        //加载player数据
        DatabaseManager.getInstance().executeQueryAsync(sqlSession -> {
            //查一堆
            return null;
        }).addListener(f -> {
           //保存信息到cache
           Executors.getInstance().getPlayerExecutor(player.getId()).execute(() -> {
               PlayerCache p = PlayerCacheManager.getInstance().getByAccountId(accountId);
               p.setPlayer((Player)f.getNow());
           });
        });
    }

}
