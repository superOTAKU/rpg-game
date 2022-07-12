package org.summer.net;

import io.netty.channel.Channel;
import org.summer.game.player.PlayerCache;
import org.summer.net.packet.Packet;

public class GameSession {
    private final Channel channel;
    private String accountId;
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

    public void kick() {

    }

    public void close() {

    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

}
