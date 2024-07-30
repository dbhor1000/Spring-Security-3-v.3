package JC.Spring.Security3.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TokenUtils {

        private static final String SECRET_KEY="23587147b9bf7a17f31444c5c47d43bb05d23ea23a4c19c21f651afd8996eb26920e12bbd8093e618a4f5aa907d90b05f97e4943ccf83127e6d930e4d7d98eefd872a42f6aa94dfb832cd252a630675989a3da8fd431f3c7a41e19d936f236f3d6b0a0c283c829d735b9402e5a747a209996644e1b6e50f135187af1929c7499d0f0e1a4cb42b162ff72cd16a24cd3a75046ad0aab1dc708b7f89f4098b2397f81c9a96be8c79b2a06d10ac5940eae323f17236e4f343b0501b4d53d04bb510fa787e5b7298707ccc5e9a05b804ef0a1bfc96e875c79aa1566d7061dd95eebc5d605ce6fa2386bf57d36d4a10c7fa34ea4a113ffb93943f62696b25c9983a49b";

        public static LocalDateTime extractExpirationTimeFromToken(String token) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                Instant expirationInstant = claims.getExpiration().toInstant();
                return LocalDateTime.ofInstant(expirationInstant, ZoneId.systemDefault());
            } catch (SignatureException | MalformedJwtException e) {
                // Handle invalid token exceptions
                return null;
            }
        }

    public static String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}