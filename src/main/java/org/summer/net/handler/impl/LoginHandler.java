package org.summer.net.handler.impl;

import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.summer.database.IdGenerator;
import org.summer.database.entity.Player;
import org.summer.database.entity.PlayerState;
import org.summer.database.entity.VocationType;
import org.summer.game.Executors;
import org.summer.game.player.PlayerCache;
import org.summer.game.player.PlayerCacheManager;
import org.summer.game.player.PlayerService;
import org.summer.login.LoginTokenManager;
import org.summer.login.Token;
import org.summer.net.*;
import org.summer.net.dto.LoginReq;
import org.summer.net.dto.LoginRsp;
import org.summer.net.handler.PacketHandler;
import org.summer.net.packet.Packet;
import org.summer.net.packet.PacketFactory;
import org.summer.util.JacksonUtil;

@Slf4j
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
        LoginReq req = JacksonUtil.getPayload(packet, LoginReq.class);
        if (req == null || StringUtils.isBlank(req.getToken())) {
            //参数不合法
            return;
        }
        Token token = LoginTokenManager.getInstance().getToken(req.getToken());
        if (token == null) {
            //token不存在，先走http设置好token
            return;
        }
        if (token.getExpiredMillis() < System.currentTimeMillis()) {
            //token过期
            return;
        }
        String accountId = token.getAccountId();
        GameSession oldSession = GameSessionManager.getInstance().getSessionByAccountId(accountId);
        if (oldSession == null) {
            GameSessionManager.getInstance().bindAccount(accountId, session);
        } else {
            if (oldSession == session) {
                //重复登录，理论上不应该出现
                return;
            } else {
                //通知客户端被挤下线
                oldSession.sendPacket(PacketFactory.errJson(OperationCodes.KICK, ErrorCodes.USER_KICK));
                //关闭旧session
                oldSession.close();
            }
        }
        PlayerCache playerCache = PlayerCacheManager.getInstance().getByAccountId(token.getAccountId());
        if (playerCache == null) {
            //同步加载player，确定player是否存在
            Player player = PlayerService.getInstance().selectByAccountId(accountId);
            if (player == null) {
                //创建Player
                player = new Player();
                player.setId(IdGenerator.getInstance().nextId());
                player.setAccountId(accountId);
                player.setVocation(VocationType.NONE);
                player.setState(PlayerState.PENDING_INFO);
                PlayerService.getInstance().createPlayer(player);
            }
            playerCache = new PlayerCache();
            playerCache.setAccountId(accountId);
            playerCache.setPlayerId(player.getId());
            playerCache.setPlayer(player);
            playerCache.setState(PlayerCache.PlayerCacheState.INIT);
        }
        playerCache.setSession(session);
        session.setPlayerCache(playerCache);
        if (playerCache.getState() == PlayerCache.PlayerCacheState.INIT || playerCache.getState() == PlayerCache.PlayerCacheState.LOADING) {
            if (playerCache.getState() == PlayerCache.PlayerCacheState.INIT) {
                playerCache.startLoginLoading();
            }
            session.setState(GameSession.SessionState.LOADING_LOGIN_DATA);
        } else if (playerCache.getState() == PlayerCache.PlayerCacheState.ACTIVE) {
            session.setState(GameSession.SessionState.ACTIVE);
        }
        //回包告知客户端登录成功
        session.sendPacket(PacketFactory.okRspJson(packet,
                LoginRsp.builder()
                        .playerId(playerCache.getPlayerId())
                        .state(session.getState())
                        .build()));
        log.info("player {} login success", playerCache.getPlayerId());
    }

}
