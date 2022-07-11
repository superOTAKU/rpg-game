package org.summer.game.player;

import org.summer.database.DatabaseManager;
import org.summer.database.entity.Player;
import org.summer.database.mapper.PlayerMapper;

//封装简易的db操作
public class PlayerService {

    public Player selectByAccountId(String accountId) {
        return DatabaseManager.getInstance().executeSync(sqlSession -> sqlSession.getMapper(PlayerMapper.class).selectByAccountId(accountId));
    }

    private static final PlayerService INSTANCE = new PlayerService();
    public static PlayerService getInstance() {
        return INSTANCE;
    }
}
