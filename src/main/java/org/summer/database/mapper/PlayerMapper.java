package org.summer.database.mapper;

import org.apache.ibatis.annotations.Select;
import org.summer.database.entity.Player;

import java.util.List;

public interface PlayerMapper {
    Player selectById(Long id);

    int insert(Player player);

    Player selectByAccountId(String accountId);

    @Select("select id,nickname from player")
    List<Player> selectPlayerNames();

}
