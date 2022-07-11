package org.summer.game.player;

import lombok.Data;
import org.summer.database.entity.Player;
import org.summer.net.GameSession;

/**
 * 玩家在内存中的数据结构
 */
@Data
public class PlayerCache {
    private Long id;
    private String accountId;
    private PlayerCacheState state;
    private Player player;
    private GameSession session;

    public enum PlayerCacheState {
        LOADING, LOADED
    }

}
