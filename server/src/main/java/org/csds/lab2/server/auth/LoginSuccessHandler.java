package org.csds.lab2.server.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.csds.lab2.server.dto.PublicKey;
import org.csds.lab2.server.security.KeyStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Authentication authentication) throws JsonProcessingException {
        String username = authentication.getName();

        request.getSession().setAttribute("username", username);
        String body = "";
        try {
            body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        PublicKey publicKey = objectMapper.readValue(body, PublicKey.class);

        KeyStore.getInstance().addSessionKey(username, "key");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
