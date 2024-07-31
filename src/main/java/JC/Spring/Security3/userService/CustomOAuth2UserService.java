package JC.Spring.Security3.userService;

import JC.Spring.Security3.repository.UserRepository;
import JC.Spring.Security3.security.CustomOAuth2User;
import JC.Spring.Security3.user.Role;
import JC.Spring.Security3.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Extract user information from oAuth2User
        String login = oAuth2User.getAttribute("login");
        if (login == null || login.isEmpty()) {
            throw new OAuth2AuthenticationException("Username cannot be empty");
        }

        // Load or create user in the database
        User user = userRepository.findByLogin(login);
        if (user == null) {
            user = new User();
            user.setLogin(login);
            user.setRole(Role.ROLE_USER); // Default role
            userRepository.save(user);
        }

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2User, user.getAuthorities());
        return customOAuth2User.getoAuth2User();
    }
}


