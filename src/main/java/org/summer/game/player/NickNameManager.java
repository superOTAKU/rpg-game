package org.summer.game.player;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.summer.database.DatabaseManager;
import org.summer.database.mapper.PlayerMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户不能取重复的昵称，程序启动前，要把当前所有用到的昵称load到内存
 */
@Slf4j
public class NickNameManager {
    private final Map<Long, String> playerNameMap = new HashMap<>();

    public void loadAll() {
        DatabaseManager.getInstance().run(sqlSession -> sqlSession.getMapper(PlayerMapper.class)
                .selectPlayerNames().forEach(player -> {
                    if (StringUtils.isNotBlank(player.getNickname())) {
                        playerNameMap.put(player.getId(), player.getNickname());
                    }
                }));
      log.info("nicknames loaded!");
    }

    public synchronized boolean allocateName(Long playerId, String name) {
        String oldName = playerNameMap.get(playerId);
        if (Objects.equals(oldName, name)) {
            return true;
        } else {
            if (!playerNameMap.containsValue(name)) {
                playerNameMap.put(playerId, name);
                return true;
            } else {
                return false;
            }
        }
    }

    private static final NickNameManager INSTANCE = new NickNameManager();
    public static NickNameManager getInstance() {
        return INSTANCE;
    }
}
