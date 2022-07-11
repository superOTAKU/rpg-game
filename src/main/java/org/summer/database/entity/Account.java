package org.summer.database.entity;

import lombok.Data;

@Data
public class Account {
    private String accountId;

    public static Account findById(String accountId) {
        return null;
    }
}
