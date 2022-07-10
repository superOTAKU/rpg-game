package org.summer.database;

import lombok.Data;

@Data
public class DatabaseConfig {
    private String url;
    private String username;
    private String password;
    private Integer idleConnCount;
    private Integer maxConnCount;
}
