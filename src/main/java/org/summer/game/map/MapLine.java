package org.summer.game.map;

/**
 * 分线，即地图的一个平行世界
 */
public class MapLine {
    //分线中所有的格子
    private MapCeil[][] ceilTable;
    private int playerCount;

    public void addPlayer(Long playerId, Position pos) {
        CellKey cellKey = getCellKeyByPos(pos);
        MapCeil cell = getCell(cellKey);
        cell.addPlayer(playerId, pos);
        playerCount++;
    }

    public void removePlayer(Long playerId) {

    }

    public void move(Long playerId, Position pos) {

    }

    public int getPlayerCount() {
        return playerCount;
    }

    private MapCeil getCell(CellKey key) {
        return ceilTable[key.getX()][key.getY()];
    }

    private CellKey getCellKeyByPos(Position pos) {
        CellKey key = new CellKey();
        key.setX(1);
        key.setY(1);
        return key;
    }

}
