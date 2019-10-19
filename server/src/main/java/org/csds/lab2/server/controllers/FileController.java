package org.csds.lab2.server.controllers;

import org.csds.lab2.server.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "files", method = RequestMethod.GET)
    public String getFile(HttpServletResponse response,
                          @RequestParam("name") String name) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return fileService.getFile(name);
    }
}
