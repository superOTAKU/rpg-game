package org.summer.game;

import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

//管理所有的业务线程池
public class Executors {
    //用户线程组
    private final DefaultEventExecutorGroup playerGroup = new DefaultEventExecutorGroup(4);
    //场景线程组
    private final DefaultEventExecutorGroup mapGroup = new DefaultEventExecutorGroup(4);
    //一条线程用于登录
    private final DefaultEventExecutor loginExecutor = new DefaultEventExecutor();
    //公共线程池，全局执行一些异步逻辑
    private final DefaultEventExecutorGroup commonGroup = new DefaultEventExecutorGroup(8);

    private final ConcurrentMap<Long, EventExecutor> playerExecutors = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, EventExecutor> mapExecutors = new ConcurrentHashMap<>();

    public EventExecutor getPlayerExecutor(Long playerId) {
        EventExecutor executor = playerExecutors.get(playerId);
        if (executor != null) {
            return executor;
        }
        playerExecutors.putIfAbsent(playerId, playerGroup.next());
        return playerExecutors.get(playerId);
    }

    public EventExecutor getMapExecutor(Long sceneId) {
        EventExecutor executor = mapExecutors.get(sceneId);
        if (executor != null) {
            return executor;
        }
        mapExecutors.putIfAbsent(sceneId, mapGroup.next());
        return mapExecutors.get(sceneId);
    }

    public EventExecutor getLoginExecutor() {
        return loginExecutor;
    }

    public EventExecutorGroup getCommonGroup() {
        return commonGroup;
    }

    private static final Executors INSTANCE = new Executors();
    public static Executors getInstance() {
        return INSTANCE;
    }

}
