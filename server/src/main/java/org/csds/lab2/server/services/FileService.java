package org.csds.lab2.server.services;

import org.csds.lab2.server.config.Configuration;
import org.csds.lab2.server.db.DBAdaptor;
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

    public String getFile(String name) {
        return dbAdaptor.getFileText(name);
    }
}
