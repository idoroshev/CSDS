package org.csds.lab2.server.dto;

public class UserPublicKey extends PublicKey {

    private String username;

    public UserPublicKey() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
