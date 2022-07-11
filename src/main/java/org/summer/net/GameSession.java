package org.summer.net;

import io.netty.channel.Channel;
import org.summer.database.entity.Player;
import org.summer.game.player.PlayerCache;

public class GameSession {
    private Channel channel;
    //没有
    private Long accountId;
    private SessionState state;
    private volatile PlayerCache player;

    public enum SessionState {
        INIT, PENDING_TOKEN, PENDING_LOGIN, LOADING_DATA, ACTIVE
    }

    public SessionState getState() {
        return state;
    }

    public PlayerCache getPlayer() {
        return player;
    }
}
