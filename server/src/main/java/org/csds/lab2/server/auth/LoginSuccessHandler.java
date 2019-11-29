package org.csds.lab2.server.auth;

import org.csds.lab2.server.security.EncryptionUtils;
import org.csds.lab2.server.security.KeyStore;
import org.csds.lab2.server.utils.EmailService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("FieldCanBeLocal")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private String subject = "Verification";
    private String textPattern = "Your verification code: ";

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
            sendVerificationCode(username);
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void sendVerificationCode(String username) {
        String code = EncryptionUtils.generateRandomKey(6);
        KeyStore.getInstance().setCode(username, code);
        EmailService.getInstance().sendEmail(subject, textPattern + code, username);
    }
}
