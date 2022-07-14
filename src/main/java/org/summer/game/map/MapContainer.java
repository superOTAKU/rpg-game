package org.summer.game.map;

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

}
