package org.summer.game.map;

import io.netty.util.concurrent.EventExecutor;

import java.util.HashMap;
import java.util.Map;

public class MapContainer {
    private Integer id;
    private MapConfig config;
    /**
     * 地图分线
     */
    private Map<Integer, MapLine> lineMap = new HashMap<>();
    //最大分线id
    private Integer maxLineId;
    /**
     * 玩家所在分线
     */
    private Map<Long, Integer> playerLineMap = new HashMap<>();

    //地图线程
    //TODO 定时器定时同步地图信息
    private EventExecutor executor;


}
