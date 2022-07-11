package org.summer.login;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LoginTokenManager {
    private final ConcurrentMap<String, Token> tokenMap = new ConcurrentHashMap<>();
    private static final long EXPIRE_TIMEOUT = 10 * 60 * 1000;

    public String addAccountToken(String accountId) {
        Token token = new Token();
        token.setAccountId(accountId);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiredMillis(System.currentTimeMillis() + EXPIRE_TIMEOUT);
        tokenMap.put(token.getToken(), token);
        return token.getToken();
    }

    public Token getToken(String token) {
        return tokenMap.get(token);
    }

    private static final LoginTokenManager INSTANCE = new LoginTokenManager();
    public static LoginTokenManager getInstance() {
        return INSTANCE;
    }
}
