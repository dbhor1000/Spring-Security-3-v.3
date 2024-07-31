package JC.Spring.Security3.security;

import JC.Spring.Security3.repository.UserRepository;
import JC.Spring.Security3.user.Role;
import JC.Spring.Security3.user.User;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import java.util.function.Supplier;

@Component
public class AdminRolePassAuthManager implements AuthorizationManager<RequestAuthorizationContext> {

    private final UserRepository userRepository;

    public AdminRolePassAuthManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userRepository.findByLogin(username);
            if (user.getRole() == Role.ROLE_ADMIN) {
                return new AuthorizationDecision(true);
            }
        }
        return new AuthorizationDecision(false);
    }
}