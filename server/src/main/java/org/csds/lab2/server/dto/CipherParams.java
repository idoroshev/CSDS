package org.csds.lab2.server.dto;

public class CipherParams {
    private String sessionKey;
    private String initVector;
    private String nextToken;

    public CipherParams() {
    }

    public CipherParams(String sessionKey, String initVector, String nextToken) {
        this.sessionKey = sessionKey;
        this.initVector = initVector;
        this.nextToken = nextToken;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getInitVector() {
        return initVector;
    }

    public void setInitVector(String initVector) {
        this.initVector = initVector;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }
}
