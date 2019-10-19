package org.csds.lab2.server.services;

import org.springframework.stereotype.Service;

@Service
public class FileService {

    public String getFile(String name) {
        return "test" + name;
    }
}
