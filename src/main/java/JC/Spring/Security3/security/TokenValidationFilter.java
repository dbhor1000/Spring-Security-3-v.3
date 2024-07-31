package JC.Spring.Security3.security;

import JC.Spring.Security3.service.TokenBlacklistService;
import JC.Spring.Security3.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

public class TokenValidationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private static final String JWT_SECRET_KEY = "b09c04f0cc8005e49da83c9c84e6fb36cfc4e0451a970eb8d77bf6ed44a3e47d7f5adf38850c23301d61826c25d74035269d3323e0e1daa241" +
            "e36d5b6e6b9a5eaf727c279dab476fada0128876123ef814df00a0fbc174f1c40fe343126551f0a4fa59ac238e4330403efb97bd38a02550e2dcf89c1464abfe0bd06ca71f9454431" +
            "d8fd82e44d4f2f7f8331a5dda25778f1374db830560b02a1cb0cd476612b0376deff9b5285e838b5a7ad11eb2efe55f9aab9b448f6b0aeb8f261b5c04f1c77f590bf5a0f26b3100" +
            "896affb2c8e82708cd187de05c7d03048b9ec8831207f89f9f7e4498ac9c623543b9a41ae0d2892f4c3e2418bd94ccc0d0093361b7b07d"; // Replace with your actual secret key

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = TokenUtils.extractTokenFromRequest(request);
       if (token != null) {
            if (tokenBlacklistService.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            try {
                Claims claims = Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
                String username = claims.get("username", String.class);
                String role = claims.get("role", String.class);

                System.out.println("TokenValidationFilter: Extracted username: " + username);
                System.out.println("TokenValidationFilter: Extracted role: " + role);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, List.of(authority));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }
}