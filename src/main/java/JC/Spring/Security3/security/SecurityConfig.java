package JC.Spring.Security3.security;

import JC.Spring.Security3.userService.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private LogoutHandler customLogoutHandler;

    @Autowired
    private AdminRolePassAuthManager databaseUserAuthorizationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/user", "/profile", "/logout", "/highSecurity").authenticated()
                        .requestMatchers("/adminOnly").access(databaseUserAuthorizationManager)
                        .anyRequest().permitAll()
                )
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll()
                        .addLogoutHandler(customLogoutHandler)
                )
                .oauth2Login(o -> o
                        .userInfoEndpoint(u -> u
                                .userService(customOAuth2UserService)
                        )
                )
                .exceptionHandling(e -> e
                        .accessDeniedHandler(new CustomAccessDeniedHandler("/roleInsufficient"))
                        .defaultAuthenticationEntryPointFor(
                                new LoginRequiredEntryPoint("/roleInsufficient"),
                                new OrRequestMatcher(
                                        new AntPathRequestMatcher("/adminOnly/**")
                                )
                        )
                );

        return http.build();
    }
}