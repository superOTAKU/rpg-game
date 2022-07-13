package org.summer.net;

import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;
import org.summer.database.entity.Player;
import org.summer.database.entity.PlayerState;
import org.summer.game.Executors;
import org.summer.game.player.PlayerCache;
import org.summer.net.packet.Packet;

import java.util.Optional;

public class GameSession {
    private final Channel channel;
    private volatile SessionState state;
    private volatile PlayerCache playerCache;

    public GameSession(Channel channel) {
        this.channel = channel;
        this.state = SessionState.PENDING_LOGIN;
    }

    public enum SessionState {
        //等待登录
        PENDING_LOGIN,
        LOADING_LOGIN_DATA,
        ACTIVE
    }

    public SessionState getState() {
        return state;
    }

    public void setState(SessionState state) {
        this.state = state;
    }

    public PlayerCache playerCache() {
        return playerCache;
    }

    public void setPlayerCache(PlayerCache player) {
        this.playerCache = player;
    }

    public Player playerEntity() {
        return Optional.ofNullable(playerCache).map(PlayerCache::getPlayer).orElse(null);
    }

    public String accountId() {
        return Optional.ofNullable(playerCache).map(PlayerCache::getAccountId).orElse(null);
    }

    public Long playerId() {
        return Optional.ofNullable(playerCache).map(PlayerCache::getPlayerId).orElse(null);
    }

    public EventExecutor playerExecutor() {
        return Optional.ofNullable(playerCache).map(PlayerCache::getPlayerId)
                .map(Executors.getInstance()::getPlayerExecutor).orElse(null);
    }

    public PlayerState playerState() {
        return Optional.ofNullable(playerCache).map(PlayerCache::getPlayerState).orElse(null);
    }

    public void close() {
        //TODO 如果有其他业务需要关闭连接前做，这里先做
        channel.close();
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

}
