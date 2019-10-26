package org.csds.lab2.server.auth;

import org.csds.lab2.server.security.KeyStore;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) {
        KeyStore.getInstance().removeSessionKey(authentication.getName());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
