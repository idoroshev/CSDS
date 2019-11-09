package org.csds.lab2.server.security;

public class UserData {

    private String sessionKey;
    private Long expirationTime;
    private String nextToken;

    public UserData(String sessionKey, Long expirationTime, String nextToken) {
        this.sessionKey = sessionKey;
        this.expirationTime = expirationTime;
        this.nextToken = nextToken;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
