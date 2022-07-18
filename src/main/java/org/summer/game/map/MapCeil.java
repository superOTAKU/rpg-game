package org.summer.game.map;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapCeil {
    //格子范围
    private int xRange;
    private int yRange;
    //玩家当前位置
    private Map<Long, Position> playerPositions;
    //玩家行走的路线
    private Map<Long, List<Position>> playerRoutes;

    //从其他格子进入当前格子的玩家
    private Set<Long> newAddPlayers;
    //离开当前格子的玩家
    private Set<Long> removedPlayers;
}
