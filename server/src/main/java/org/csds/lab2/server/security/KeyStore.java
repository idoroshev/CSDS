package org.csds.lab2.server.security;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class KeyStore {

    private static int MILLISECONDS_IN_MINUTE = 1000 * 60;

    private Map<String, UserData> keyStorage = new HashMap<>();

    private static KeyStore ourInstance = new KeyStore();

    public static KeyStore getInstance() {
        return ourInstance;
    }

    private KeyStore() {
    }

    public void addSessionKey(String username, String key) {
        keyStorage.put(username, new UserData(key, System.currentTimeMillis() + 2 * MILLISECONDS_IN_MINUTE, null));
    }

    public boolean expired(String username) {
        return keyStorage.containsKey(username) && keyStorage.get(username).getExpirationTime() < System.currentTimeMillis();
    }

    public String getSessionKey(String username) {
        if (keyStorage.containsKey(username)) {
            return keyStorage.get(username).getSessionKey();
        } else {
            return null;
        }
    }

    public boolean checkNextToken(String username, String clientToken) {
        if (keyStorage.containsKey(username)) {
            String nextToken = keyStorage.get(username).getNextToken();
            String decryptedClientToken = EncryptionUtils.decryptByAES(clientToken, keyStorage.get(username).getSessionKey());
            return decryptedClientToken != null && decryptedClientToken.equals(nextToken + username);
        } else {
            return false;
        }
    }

    public String getNextToken(String username) {
        if (keyStorage.containsKey(username)) {
            String nextToken = EncryptionUtils.generateRandomKey();
            keyStorage.get(username).setNextToken(nextToken);
            return EncryptionUtils.encryptByAES(nextToken, keyStorage.get(username).getSessionKey());
        } else {
            return null;
        }
    }

    public void removeSessionKey(String username) {
        keyStorage.remove(username);
    }

    public void setCode(String username, String code) {
        UserData userData = keyStorage.get(username);
        if (userData != null) {
            userData.setVerificationCode(code);
            userData.setVerificationExpiration(System.currentTimeMillis() + 3 * MILLISECONDS_IN_MINUTE);
            userData.setVerified(false);
        }
    }

    public boolean isVerified(String username) {
        UserData userData = keyStorage.get(username);
        if (userData != null) {
            return userData.isVerified();
        } else {
            return false;
        }
    }

    public boolean checkCode(String username, String code) {
        UserData userData = keyStorage.get(username);
        if (userData != null) {
            if (!userData.isForbidVerification() && userData.getVerificationCode().equals(code) && System.currentTimeMillis() < userData.getVerificationExpiration()) {
                userData.setVerified(true);
            } else {
                userData.setVerified(false);
                userData.setForbidVerification(true);
            }
            return userData.isVerified();
        } else {
            return false;
        }
    }
}
