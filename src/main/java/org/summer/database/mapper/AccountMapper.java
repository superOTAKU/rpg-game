package org.summer.database.mapper;

import org.apache.ibatis.annotations.Select;
import org.summer.database.entity.Account;

public interface AccountMapper {
    @Select("select * from account where id=#{id}")
    Account selectById(String id);
}
