package org.csds.lab2.server.services;

import org.csds.lab2.server.dto.CipherParams;
import org.csds.lab2.server.dto.UserPublicKey;
import org.csds.lab2.server.security.EncryptionUtils;
import org.csds.lab2.server.security.KeyStore;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public CipherParams createSessionKey(UserPublicKey publicKey) {
        String sessionKey = EncryptionUtils.generateRandomKey();
        String initVector = EncryptionUtils.initVector;
        System.out.println("session key: " + sessionKey);
        KeyStore.getInstance().addSessionKey(publicKey.getUsername(), sessionKey);
        return new CipherParams(EncryptionUtils.encryptByRSA(sessionKey, publicKey),
                EncryptionUtils.encryptByRSA(initVector, publicKey),
                KeyStore.getInstance().getNextToken(publicKey.getUsername()));
    }

}
