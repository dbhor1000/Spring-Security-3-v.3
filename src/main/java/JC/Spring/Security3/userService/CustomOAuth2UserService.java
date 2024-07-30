package JC.Spring.Security3.userService;

import JC.Spring.Security3.repository.UserRepository;
import JC.Spring.Security3.user.Role;
import JC.Spring.Security3.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Integer loginId = (Integer) attributes.get("id");
        String login = (String) attributes.get("login");

        User user = userRepository.findByLogin(login);
        if (user == null) {
            user = new User();
            user.setLogin(login);
            user.setLoginId(loginId);
            user.setRole(Role.USER); // Default role
            userRepository.save(user);
        }

        return oAuth2User;
    }
}
