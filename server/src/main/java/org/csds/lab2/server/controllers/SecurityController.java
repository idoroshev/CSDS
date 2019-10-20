package org.csds.lab2.server.controllers;

import org.csds.lab2.server.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SecurityController {

    private SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping(value = "open_key", method = RequestMethod.POST)
    public void saveOpenKey(@RequestBody String openKey) {

        securityService.saveOpenKey(openKey);
    }

    @RequestMapping(value = "session_key", method = RequestMethod.GET)
    public String getSessionKey(HttpServletRequest request, HttpServletResponse response) {
        String username = (String) request.getSession().getAttribute("username");
        if (username != null) {
            System.out.println(username);
        }
        return username + securityService.getSessionKey();
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public void login(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
