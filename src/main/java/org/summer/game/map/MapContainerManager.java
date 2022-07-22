package org.summer.game.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 提供给外部调用的外观接口
 *
 * TODO 地图管理一个线程，每个地图内部一个线程
 *
 */
public class MapContainerManager {
    //所有的地图实例
    private Map<Integer, MapContainer> mapContainerMap = new HashMap<>();
    //玩家对应的地图
    private Map<Long, Integer> playerToMap = new HashMap<>();

    public void enterMap(Long playerId, Integer mapId, Integer pointedLine, Position pos) {
        Integer oldMapId = playerToMap.get(playerId);
        if (oldMapId != null) {
            if (!Objects.equals(mapId, oldMapId)) {
                mapContainerMap.get(oldMapId).leave(playerId);
                mapContainerMap.get(mapId).enter(playerId, pointedLine, pos);
                playerToMap.put(playerId, mapId);
            } else {
                mapContainerMap.get(mapId).enter(playerId, pointedLine, pos);
            }
        } else {
            mapContainerMap.get(mapId).enter(playerId, pointedLine, pos);
            playerToMap.put(playerId, mapId);
        }
    }

    public void move(Long playerId, Integer pointedLine, Position pos) {
        Integer oldMapId = playerToMap.get(playerId);
        if (oldMapId == null) {
            return;
        }
        mapContainerMap.get(oldMapId).move(playerId, pointedLine, pos);
    }

}
