package org.csds.lab2.server.security;

public class UserData {

    private String sessionKey;
    private Long expirationTime;
    private String nextToken;
    private String verificationCode;
    private boolean verified = false;
    private Long verificationExpiration;
    private boolean forbidVerification = false;

    public UserData(String sessionKey, Long expirationTime, String nextToken) {
        this.sessionKey = sessionKey;
        this.expirationTime = expirationTime;
        this.nextToken = nextToken;
        this.verified = false;
        this.forbidVerification = false;
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Long getVerificationExpiration() {
        return verificationExpiration;
    }

    public void setVerificationExpiration(Long verificationExpiration) {
        this.verificationExpiration = verificationExpiration;
    }

    public boolean isForbidVerification() {
        return forbidVerification;
    }

    public void setForbidVerification(boolean forbidVerification) {
        this.forbidVerification = forbidVerification;
    }
}
