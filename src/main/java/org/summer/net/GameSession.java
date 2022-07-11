package org.summer.net;

import io.netty.channel.Channel;
import org.summer.database.entity.Player;
import org.summer.game.player.PlayerCache;
import org.summer.net.packet.Packet;

public class GameSession {
    private Channel channel;
    //没有
    private String accountId;
    private volatile SessionState state;
    private volatile PlayerCache player;

    public enum SessionState {
        PENDING_LOGIN,
        LOADING_LOGIN_DATA,
        ACTIVE
    }

    public SessionState getState() {
        return state;
    }

    public PlayerCache getPlayer() {
        return player;
    }

    public void kick() {

    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

}
