package org.summer.login;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Token {
    private String accountId;
    private String token;
    private Long expiredMillis;
}
