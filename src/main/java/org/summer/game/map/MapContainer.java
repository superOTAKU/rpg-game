package org.summer.game.map;

import io.netty.util.concurrent.EventExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private static final int MAX_PLAYER_PER_LINE = 100;

    /**
     * 玩家进入地图
     * @param playerId 玩家id
     * @param pointedLine 是否指定分线，如果指定，不为null
     * @param pos 具体位置
     */
    public void enter(Long playerId, Integer pointedLine, Position pos) {
        MapLine line = allocateLine(playerId, pointedLine);
        line.addPlayer(playerId, pos);
    }

    /**
     * 玩家在地图中移动（不换线）
     */
    public void move(Long playerId, Position pos) {
        move(playerId, null, pos);
    }

    /**
     * 玩家在地图中移动（可能换线）
     */
    public void move(Long playerId, Integer pointedLine, Position pos) {
        Integer lineId = playerLineMap.get(playerId);
        if (lineId == null) {
            if (pointedLine != null) {
                lineId = pointedLine;
            } else {
                allocateLine(playerId, null);
                lineId = playerLineMap.get(playerId);
            }
            lineMap.get(lineId).addPlayer(playerId, pos);
        } else {
            if (pointedLine != null && !Objects.equals(lineId, pointedLine)) {
                lineMap.get(lineId).removePlayer(playerId);
                lineMap.get(pointedLine).addPlayer(playerId, pos);
            } else {
                lineMap.get(lineId).move(playerId, pos);
            }
        }
    }

    //玩家离开地图，切地图的时候用到
    public void leave(Long playerId) {
        Integer lineId = playerLineMap.get(playerId);
        if (lineId != null) {
            lineMap.get(lineId).removePlayer(playerId);
        }
    }

    //给玩家配线
    private MapLine allocateLine(Long playerId, Integer pointedLine) {
        Integer lineId = playerLineMap.get(playerId);
        if (lineId != null) {
            playerLineMap.remove(playerId);
            lineMap.get(lineId).removePlayer(playerId);
        }
        if (pointedLine != null) {
            return lineMap.get(pointedLine);
        } else {
            return doAllocateLine();
        }
    }

    private MapLine doAllocateLine() {
        for (int i = 1; i <= maxLineId; i++) {
            MapLine line = lineMap.get(i);
            if (line.getPlayerCount() < MAX_PLAYER_PER_LINE) {
                return line;
            }
        }
        int newLineId = ++maxLineId;
        lineMap.put(newLineId, new MapLine());
        return lineMap.get(newLineId);
    }

}
