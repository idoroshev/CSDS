package org.csds.lab2.server.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) {
        String username = authentication.getName();

        request.getSession().setAttribute("username", username);
        KeyStore.getInstance().addSessionKey(username, "key");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
