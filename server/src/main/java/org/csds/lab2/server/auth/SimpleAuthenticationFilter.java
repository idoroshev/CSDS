package org.csds.lab2.server.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.csds.lab2.server.dto.User;
import org.csds.lab2.server.security.EncryptionUtils;
import org.csds.lab2.server.security.KeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SimpleAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    public SimpleAuthenticationFilter(AuthenticationManager manager) {
        super.setAuthenticationManager(manager);
        super.setAuthenticationSuccessHandler(new LoginSuccessHandler());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String body = "";
        try {
            body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(body, User.class);
            String sessionKey = KeyStore.getInstance().getSessionKey(user.getUsername());
            if (sessionKey != null && KeyStore.getInstance().checkNextToken(user.getUsername(), request.getParameter("nextToken"))) {
                System.out.println("Encrypted pass: " + user.getPassword());
                String decryptedPassword = EncryptionUtils.decryptByAES(user.getPassword(), sessionKey);
                user.setPassword(decryptedPassword);

                System.out.println("Decrypted pass: " + decryptedPassword);
                UsernamePasswordAuthenticationToken token
                        = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

                setDetails(request, token);
                return this.getAuthenticationManager().authenticate(token);

            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }
}
