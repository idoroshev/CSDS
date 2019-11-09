package org.csds.lab2.server.controllers;

import org.csds.lab2.server.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "files", method = RequestMethod.GET)
    public String getFile(HttpServletResponse response,
                          @RequestParam("username") String username,
                          @RequestParam("name") String name) {
        System.out.println(response);
        try {
            return fileService.getFile(name, username);
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getLocalizedMessage());
            } catch (IOException ex) {
                System.out.println("Cannot send error message.");
            }
        } catch (IllegalAccessException e) {
            response.setStatus(419);
            try {
                response.sendError(419, e.getLocalizedMessage());
            } catch (IOException ex) {
                System.out.println("Cannot send error message.");
            }
        }
        return null;
    }
}
