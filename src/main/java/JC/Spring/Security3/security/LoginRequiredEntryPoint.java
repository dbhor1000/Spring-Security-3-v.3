package JC.Spring.Security3.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginRequiredEntryPoint implements AuthenticationEntryPoint {

    private final String redirectUri;

    public LoginRequiredEntryPoint(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect(redirectUri);
    }
}