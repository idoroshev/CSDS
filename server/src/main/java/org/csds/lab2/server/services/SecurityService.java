package org.csds.lab2.server.services;

import org.csds.lab2.server.dto.UserPublicKey;
import org.csds.lab2.server.security.EncryptionUtils;
import org.csds.lab2.server.security.KeyStore;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String createSessionKey(UserPublicKey publicKey) {
        String sessionKey = EncryptionUtils.generateSessionKey();
        System.out.println("session key: " + sessionKey);
        KeyStore.getInstance().addSessionKey(publicKey.getUsername(), sessionKey);
        return EncryptionUtils.encryptSessionKey(sessionKey, publicKey);
    }

}
