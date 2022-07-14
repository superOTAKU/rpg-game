package org.summer.game.map;

/**
 * 地图上的一个物品，可以是人物，NPC，掉落物
 */
public class MapItem {
    private int x;
    private int y;

    public enum MapItemType {
        PLAYER, NPC, DROP_ITEM
    }

}
