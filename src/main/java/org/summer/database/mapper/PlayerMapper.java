package org.summer.database.mapper;

import org.summer.database.entity.Player;

public interface PlayerMapper {
    Player selectById(Long id);

    int insert(Player player);

}
