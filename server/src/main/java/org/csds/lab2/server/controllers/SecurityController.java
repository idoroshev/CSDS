package org.csds.lab2.server.controllers;

import org.csds.lab2.server.dto.UserPublicKey;
import org.csds.lab2.server.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class SecurityController {

    private SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping(value = "session", method = RequestMethod.POST)
    public String getSessionKey(@RequestBody UserPublicKey key) {
        return securityService.createSessionKey(key);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public void login(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public void home() {
    }
}
