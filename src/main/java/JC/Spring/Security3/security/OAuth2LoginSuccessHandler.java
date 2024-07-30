package JC.Spring.Security3.security;

import JC.Spring.Security3.repository.UserRepository;
import JC.Spring.Security3.tokenManagement.JwtTokenProvider;
import JC.Spring.Security3.user.Role;
import JC.Spring.Security3.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Set;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String login = oauth2User.getAttribute("login"); // Use appropriate attribute

        // Check if the user already exists
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new IllegalStateException("User not found after authentication");
        }

        Role role = user.getRole();
        System.out.println("User role: " + role); // Add logging

        String token = JwtTokenProvider.generateToken(user.getLoginId().toString(), user.getLogin(), role);

        // Set the token as a cookie
        response.addCookie(createCookie(token));
        setDefaultTargetUrl("/home");

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Ensure the cookie is only sent over HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 hour
        return cookie;
    }
}