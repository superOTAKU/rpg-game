package org.summer.game.player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PlayerCacheManager {
    private final ConcurrentMap<Long, PlayerCache> playerMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, PlayerCache> accountPlayerMap = new ConcurrentHashMap<>();

    public PlayerCache getByAccountId(String accountId) {
        return accountPlayerMap.get(accountId);
    }

    private static final PlayerCacheManager INSTANCE = new PlayerCacheManager();
    public static PlayerCacheManager getInstance() {
        return INSTANCE;
    }
}
