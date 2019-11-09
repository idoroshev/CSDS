package org.csds.lab2.server.auth;

import org.csds.lab2.server.security.KeyStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = (String) authentication.getPrincipal();
        if (username != null) {
            response.getWriter()
                    .append("{\"nextToken\": \"")
                    .append(KeyStore.getInstance().getNextToken(username))
                    .append("\" }");
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
