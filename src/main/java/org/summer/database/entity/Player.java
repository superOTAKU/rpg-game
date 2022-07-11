package org.summer.database.entity;

import lombok.Data;

@Data
public class Player {
    private Long id;
    private String accountId;
    private String nickname;
    private VocationType vocation;
}
