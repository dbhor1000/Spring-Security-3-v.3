package JC.Spring.Security3.security;

import JC.Spring.Security3.user.Role;
import JC.Spring.Security3.userService.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new TokenValidationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/user", "/profile", "/logout", "/highSecurity").authenticated()
                        .requestMatchers("/adminOnly").hasRole("ADMIN")
                        .anyRequest().permitAll()
                )
                .logout(l -> l
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/loggedOut").permitAll()
                        .addLogoutHandler(new SecurityContextLogoutHandler())
                )
                .oauth2Login(o -> o
                        .successHandler(oAuth2LoginSuccessHandler)
                        .userInfoEndpoint(u -> u
                                .userService(customOAuth2UserService)
                        )
                )
                .exceptionHandling(e -> e
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .defaultAuthenticationEntryPointFor(
                                new LoginRequiredEntryPoint("/loginRequired"),
                                new OrRequestMatcher(
                                        new AntPathRequestMatcher("/user/**"),
                                        new AntPathRequestMatcher("/profile/**"),
                                        new AntPathRequestMatcher("/logout"),
                                        new AntPathRequestMatcher("/highSecurity/**"),
                                        new AntPathRequestMatcher("/adminOnly/**")
                                )
                        )
                );

        return http.build();
    }
}