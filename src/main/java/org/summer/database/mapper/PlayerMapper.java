package org.summer.database.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.summer.database.entity.Player;
import org.summer.database.entity.PlayerState;
import org.summer.database.entity.VocationType;

import java.util.List;

public interface PlayerMapper {
    Player selectById(Long id);

    int insert(Player player);

    Player selectByAccountId(String accountId);

    @Select("select id,nickname from player")
    List<Player> selectPlayerNames();

    @Update("update player set nickname=#{nickname} and state=#{state} where id=#{playerId}")
    int updateNickname(Long playerId, String nickname, PlayerState state);

    @Update("update player set vocation=#{vocation} and state=#{state} where id=#{playerId}")
    int updateVocation(Long playerId, VocationType vocation, PlayerState state);

}
