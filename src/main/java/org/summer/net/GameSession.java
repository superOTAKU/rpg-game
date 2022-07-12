package org.summer.net;

import io.netty.channel.Channel;
import io.netty.util.concurrent.EventExecutor;
import org.summer.game.Executors;
import org.summer.game.player.PlayerCache;
import org.summer.net.packet.Packet;

public class GameSession {
    private final Channel channel;
    private volatile SessionState state;
    private volatile PlayerCache player;

    public GameSession(Channel channel) {
        this.channel = channel;
    }

    public enum SessionState {
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

    public PlayerCache getPlayer() {
        return player;
    }

    public void setPlayer(PlayerCache player) {
        this.player = player;
    }

    public String getAccountId() {
        if (player != null) {
            return player.getAccountId();
        } else {
            return null;
        }
    }

    public EventExecutor playerExecutor() {
        if (player != null) {
            return Executors.getInstance().getPlayerExecutor(player.getPlayerId());
        } else {
            return null;
        }
    }


    public void close() {
        //TODO 如果有其他业务需要关闭连接前做，这里先做
        channel.close();
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

}
