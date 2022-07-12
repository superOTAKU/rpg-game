package org.summer.game.player;

import org.summer.database.DatabaseManager;
import org.summer.database.entity.Player;
import org.summer.database.mapper.PlayerMapper;

//封装简易的db操作
public class PlayerService {

    public Player selectByAccountId(String accountId) {
        return DatabaseManager.getInstance().supply(sqlSession -> sqlSession.getMapper(PlayerMapper.class).selectByAccountId(accountId));
    }

    public void createPlayer(Player player) {
        DatabaseManager.getInstance().run(sqlSession -> sqlSession.getMapper(PlayerMapper.class).insert(player));
    }

    private static final PlayerService INSTANCE = new PlayerService();
    public static PlayerService getInstance() {
        return INSTANCE;
    }
}
