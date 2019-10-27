package org.csds.lab2.server.services;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getSessionKey() {
        return "yoshi";
    }

    public void saveOpenKey(String openKey) {

    }
}
