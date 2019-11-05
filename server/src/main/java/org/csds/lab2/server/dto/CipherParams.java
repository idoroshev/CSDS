package org.csds.lab2.server.dto;

public class CipherParams {
    private String sessionKey;
    private String initVector;

    public CipherParams() {
    }

    public CipherParams(String sessionKey, String initVector) {
        this.sessionKey = sessionKey;
        this.initVector = initVector;
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
}
