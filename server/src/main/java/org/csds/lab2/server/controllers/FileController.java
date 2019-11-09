package org.csds.lab2.server.controllers;

import org.csds.lab2.server.dto.File;
import org.csds.lab2.server.dto.UploadFile;
import org.csds.lab2.server.security.KeyStore;
import org.csds.lab2.server.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public File getFile(HttpServletResponse response,
                        @RequestParam("username") String username,
                        @RequestParam("name") String name,
                        @RequestParam("nextToken") String clientToken) {
        try {
            return fileService.getFile(name, username, clientToken);
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                String nextToken = KeyStore.getInstance().getNextToken(username);
                if (nextToken != null) {
                    String message = "{\"nextToken\": \"" + nextToken + "\", \"text\": \"" + e.getLocalizedMessage() + "\" }";
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getLocalizedMessage());
                }
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

    @RequestMapping(value = "/files", method = RequestMethod.POST)
    public void uploadFile(HttpServletResponse response,
                           @RequestParam("username") String username,
                           @RequestParam("nextToken") String clientToken,
                           @RequestBody UploadFile file) {
        try {
            String nextToken = fileService.uploadFile(file.getName(), file.getText(), username, clientToken);
            if (nextToken != null) {
                try {
                    response.getWriter().print("{\"nextToken\": \"" + nextToken + "\" }");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                String nextToken = KeyStore.getInstance().getNextToken(username);
                if (nextToken != null) {
                    String message = "{\"nextToken\": \"" + nextToken + "\", \"text\": \"" + e.getLocalizedMessage() + "\" }";
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getLocalizedMessage());
                }
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
    }
}
