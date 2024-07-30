package JC.Spring.Security3.security;

import JC.Spring.Security3.service.TokenBlacklistService;
import JC.Spring.Security3.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static final String SECRET_KEY="23587147b9bf7a17f31444c5c47d43bb05d23ea23a4c19c21f651afd8996eb26920e12bbd8093e618a4f5aa907d90b05f97e4943ccf83127e6d930e4d7d98eefd872a42f6aa94dfb832cd252a630675989a3da8fd431f3c7a41e19d936f236f3d6b0a0c283c829d735b9402e5a747a209996644e1b6e50f135187af1929c7499d0f0e1a4cb42b162ff72cd16a24cd3a75046ad0aab1dc708b7f89f4098b2397f81c9a96be8c79b2a06d10ac5940eae323f17236e4f343b0501b4d53d04bb510fa787e5b7298707ccc5e9a05b804ef0a1bfc96e875c79aa1566d7061dd95eebc5d605ce6fa2386bf57d36d4a10c7fa34ea4a113ffb93943f62696b25c9983a49b";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = TokenUtils.extractTokenFromRequest(request);
        if (token != null) {
            if (tokenBlacklistService.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            try {
                Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
                String username = claims.get("username", String.class);
                String role = claims.get("role", String.class);

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
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