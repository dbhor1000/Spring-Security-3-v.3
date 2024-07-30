package JC.Spring.Security3.controller;

import JC.Spring.Security3.service.TokenBlacklistService;
import JC.Spring.Security3.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

@Controller
public class LogoutController {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            String token = TokenUtils.extractTokenFromRequest(request);
            if (token != null) {
                LocalDateTime expirationTime = TokenUtils.extractExpirationTimeFromToken(token);
                tokenBlacklistService.addToBlacklist(token, expirationTime);
            }
        }
    }

    @GetMapping("/loggedOut")
    public String loggedOut() {
        return "loggedOut"; // Return the name of your home view
    }
}