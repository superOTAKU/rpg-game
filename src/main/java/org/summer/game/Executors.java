package org.summer.game;

import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//管理所有的业务线程池
public class Executors {
    //用户线程组
    private final DefaultEventExecutorGroup playerGroup = new DefaultEventExecutorGroup(1);
    //场景线程组
    private final DefaultEventExecutorGroup sceneGroup = new DefaultEventExecutorGroup(1);

    private final ConcurrentMap<Long, EventExecutor> playerExecutors = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, EventExecutor> sceneExecutors = new ConcurrentHashMap<>();

    public EventExecutor getPlayerExecutor(Long playerId) {
        EventExecutor executor = playerExecutors.get(playerId);
        if (executor != null) {
            return executor;
        }
        playerExecutors.putIfAbsent(playerId, playerGroup.next());
        return playerExecutors.get(playerId);
    }

    public EventExecutor getSceneExecutors(Long sceneId) {
        EventExecutor executor = sceneExecutors.get(sceneId);
        if (executor != null) {
            return executor;
        }
        sceneExecutors.putIfAbsent(sceneId, sceneGroup.next());
        return sceneExecutors.get(sceneId);
    }

    private static final Executors INSTANCE = new Executors();
    public static Executors getInstance() {
        return INSTANCE;
    }

}
