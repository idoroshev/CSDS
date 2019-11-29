package org.csds.lab2.server.services;

import org.csds.lab2.server.config.Configuration;
import org.csds.lab2.server.db.DBAdaptor;
import org.csds.lab2.server.dto.File;
import org.csds.lab2.server.security.EncryptionUtils;
import org.csds.lab2.server.security.KeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @SuppressWarnings("FieldCanBeLocal")
    private Configuration config;
    private DBAdaptor dbAdaptor;

    @Autowired
    public FileService(Configuration config) {
        this.config = config;
        dbAdaptor = new DBAdaptor(config.getJdbcUrl());
    }

    public File getFile(String name, String username, String clientToken) throws IllegalAccessException, IllegalStateException {
        String text = dbAdaptor.getFileText(name);
        String sessionKey = KeyStore.getInstance().getSessionKey(username);
        if (sessionKey == null) {
            throw new IllegalStateException("Session key is not found for user: " + username);
        } else if (KeyStore.getInstance().expired(username)) {
            throw new IllegalAccessException("Session key is expired for user: " + username);
        } else if (!KeyStore.getInstance().isVerified(username)) {
            throw new IllegalAccessException("2FA was not passed for user: " + username);
        } else if (text == null) {
            throw new IllegalStateException("File with name " + name + "is not found.");
        } else if (!KeyStore.getInstance().checkNextToken(username, clientToken)) {
            throw new IllegalAccessException("Bad client token for user: " + username);
        } else {
            return new File(EncryptionUtils.encryptByAES(text, sessionKey), KeyStore.getInstance().getNextToken(username));
        }
    }

    public String uploadFile(String name, String text, String username, String clientToken) throws IllegalAccessException, IllegalStateException {
        String sessionKey = KeyStore.getInstance().getSessionKey(username);
        if (sessionKey == null) {
            throw new IllegalStateException("Session key is not found for user: " + username);
        } else if (KeyStore.getInstance().expired(username)) {
            throw new IllegalAccessException("Session key is expired for user: " + username);
        } else if (!KeyStore.getInstance().isVerified(username)) {
            throw new IllegalAccessException("2FA was not passed for user: " + username);
        } else if (!KeyStore.getInstance().checkNextToken(username, clientToken)) {
            throw new IllegalAccessException("Bad client token for user: " + username);
        } else {
            String decrypted = EncryptionUtils.decryptByAES(text, sessionKey);
            if (decrypted != null && dbAdaptor.insertFile(name, decrypted)) {
                return KeyStore.getInstance().getNextToken(username);
            } else {
                throw new IllegalStateException("Cannot upload file for user: " + username);
            }
        }
    }
}
