package JC.Spring.Security3.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final Collection<? extends GrantedAuthority> customAuthorities;

    public CustomOAuth2User(OAuth2User oAuth2User, Collection<? extends GrantedAuthority> customAuthorities) {
        this.oAuth2User = oAuth2User;
        this.customAuthorities = customAuthorities;
    }

    public Collection<? extends GrantedAuthority> getCustomAuthorities() {
        return customAuthorities;
    }

    public OAuth2User getoAuth2User() {
        return oAuth2User;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> combinedAuthorities = new HashSet<>(oAuth2User.getAuthorities());
        combinedAuthorities.add(new SimpleGrantedAuthority("USER"));
        combinedAuthorities.addAll(customAuthorities);
        return combinedAuthorities;
    }

    @Override
    public String getName() {
        return "";
    }

    // Implement other methods from OAuth2User interface
}