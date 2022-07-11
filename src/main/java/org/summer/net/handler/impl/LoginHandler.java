package org.summer.net.handler.impl;

import io.netty.util.concurrent.EventExecutor;
import org.apache.commons.lang3.StringUtils;
import org.summer.database.DatabaseManager;
import org.summer.database.entity.Player;
import org.summer.database.mapper.PlayerMapper;
import org.summer.game.Executors;
import org.summer.game.player.PlayerCache;
import org.summer.game.player.PlayerCacheManager;
import org.summer.game.player.PlayerService;
import org.summer.login.LoginTokenManager;
import org.summer.login.Token;
import org.summer.net.GameSession;
import org.summer.net.GameSessionManager;
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
        if (session.getState() != GameSession.SessionState.PENDING_LOGIN) {
            //session状态
            return;
        }
        LoginReq req = LoginReq.parse(packet);
        if (req == null || StringUtils.isBlank(req.getToken())) {
            //参数不合法
            return;
        }
        Token token = LoginTokenManager.getInstance().getToken(req.getToken());
        if (token == null) {
            //token不存在，先走http设置好token
            return;
        }
        if (token.getExpiredMillis() > System.currentTimeMillis()) {
            //token过期
            return;
        }
        String accountId = token.getAccountId();
        GameSession oldSession = GameSessionManager.getInstance().getSessionByAccountId(accountId);
        if (oldSession == session) {
            //重复登录，理论上不应该出现
            return;
        } else {
            //踢掉旧session
            oldSession.kick();
        }
        PlayerCache playerCache = PlayerCacheManager.getInstance().getByAccountId(token.getAccountId());
        if (playerCache == null) {
            //同步加载player，确定player是否存在
            Player player = PlayerService.getInstance().selectByAccountId(accountId);
            if (player == null) {
                //创建Player
            }
        }
        //加载player数据
        DatabaseManager.getInstance().executeQueryAsync(sqlSession -> {
            //查一堆
            return null;
        }).addListener(f -> {
           //保存信息到cache
           Executors.getInstance().getPlayerExecutor(playerCache.getId()).execute(() -> {
               PlayerCache p = PlayerCacheManager.getInstance().getByAccountId(accountId);
               p.setPlayer((Player)f.getNow());
           });
        });
    }

}
