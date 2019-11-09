package org.csds.lab2.server.security;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class KeyStore {

    private static int MILLISECONDS_IN_MINUTE = 1000 * 60;

    private Map<String, Pair<String, Long>> keyStorage = new HashMap<>();

    private static KeyStore ourInstance = new KeyStore();

    public static KeyStore getInstance() {
        return ourInstance;
    }

    private KeyStore() {
    }

    public void addSessionKey(String username, String key) {
        keyStorage.put(username, new Pair<>(key, System.currentTimeMillis() + 2 * MILLISECONDS_IN_MINUTE));
    }

    public boolean expired(String username) {
        return keyStorage.containsKey(username) && keyStorage.get(username).getValue() < System.currentTimeMillis();
    }

    public String getSessionKey(String username) {
        if (keyStorage.containsKey(username)) {
            return keyStorage.get(username).getKey();
        } else {
            return null;
        }
    }

    public void removeSessionKey(String username) {
        keyStorage.remove(username);
    }
}
