package org.summer.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GameSessionManager {
    private final ConcurrentMap<Channel, GameSession> sessionMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, GameSession> accountSessionMap = new ConcurrentHashMap<>();

    public GameSession getSession(Channel channel) {
        return sessionMap.get(channel);
    }

    public GameSession getSession(ChannelHandlerContext ctx) {
        return sessionMap.get(ctx.channel());
    }

    public GameSession getSessionByAccountId(String accountId) {
        return accountSessionMap.get(accountId);
    }

    public void addSession(Channel channel, GameSession session) {
        sessionMap.put(channel, session);
    }

    public void bindAccount(String accountId, GameSession session) {
        accountSessionMap.put(accountId, session);
    }

    public void removeSession(Channel channel) {
        GameSession session = sessionMap.remove(channel);
        if (session != null) {
            if (session.accountId() != null) {
                accountSessionMap.remove(session.accountId());
                session.playerCache().setSession(null);
            }
        }
    }

    //singleton
    private static final GameSessionManager INSTANCE = new GameSessionManager();
    public static GameSessionManager getInstance() {
        return INSTANCE;
    }

}
