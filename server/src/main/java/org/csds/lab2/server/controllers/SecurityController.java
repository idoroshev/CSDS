package org.csds.lab2.server.controllers;

import org.csds.lab2.server.dto.CipherParams;
import org.csds.lab2.server.dto.UserPublicKey;
import org.csds.lab2.server.security.KeyStore;
import org.csds.lab2.server.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class SecurityController {

    private SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping(value = "session", method = RequestMethod.POST)
    public CipherParams getSessionKey(@RequestBody UserPublicKey key) {
        return securityService.createSessionKey(key);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public void login(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @RequestMapping(value = "verify", method = RequestMethod.POST)
    public void verifyCode(HttpServletResponse response,
                           @RequestParam("username") String username,
                           @RequestParam("code") String code,
                           @RequestParam("nextToken") String clientToken) {
        if (!KeyStore.getInstance().checkNextToken(username, clientToken)) {
            response.setStatus(419);
            try {
                response.sendError(419, "Bad client token for user: " + username);
            } catch (IOException ex) {
                System.out.println("Cannot send error message.");
            }
            return;
        }
        if (KeyStore.getInstance().checkCode(username, code)) {
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                response.getWriter()
                        .append("{\"nextToken\": \"")
                        .append(KeyStore.getInstance().getNextToken(username))
                        .append("\" }");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            response.setStatus(423);
            try {
                response.sendError(419, "Wrong or expired verification code for user: " + username);
            } catch (IOException ex) {
                System.out.println("Cannot send error message.");
            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void home() {
    }
}
