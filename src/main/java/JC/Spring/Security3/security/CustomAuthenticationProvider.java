package JC.Spring.Security3.security;

import JC.Spring.Security3.repository.UserRepository;
import JC.Spring.Security3.user.Role;
import JC.Spring.Security3.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            String username = oauth2Token.getName();

            // Fetch the user details from the database or any other source
            User user = userRepository.findByLogin(username);
            if (user == null) {
                throw new AuthenticationException("User not found") {};
            }

            // Get the role from the user details
            Role role = user.getRole();
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getAuthority());

            // Create a new authentication token with the role
            return new UsernamePasswordAuthenticationToken(username, null, List.of(authority));
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2AuthenticationToken.class.isAssignableFrom(authentication);
    }
}