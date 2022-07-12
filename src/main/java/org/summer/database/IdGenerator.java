package org.summer.database;

import cn.hutool.core.lang.Snowflake;

public class IdGenerator {
    private final Snowflake snowflake = new Snowflake();

    private static final IdGenerator INSTANCE = new IdGenerator();
    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    public Long nextId() {
        return snowflake.nextId();
    }

    public String nextIdStr() {
        return snowflake.nextIdStr();
    }
}
