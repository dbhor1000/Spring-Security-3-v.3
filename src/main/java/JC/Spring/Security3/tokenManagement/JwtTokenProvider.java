package JC.Spring.Security3.tokenManagement;

import JC.Spring.Security3.user.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenProvider {

    private static String SECRET_KEY="23587147b9bf7a17f31444c5c47d43bb05d23ea23a4c19c21f651afd8996eb26920e12bbd8093e618a4f5aa907d90b05f97e4943ccf83127e6d930e4d7d98eefd872a42f6aa94dfb832cd252a630675989a3da8fd431f3c7a41e19d936f236f3d6b0a0c283c829d735b9402e5a747a209996644e1b6e50f135187af1929c7499d0f0e1a4cb42b162ff72cd16a24cd3a75046ad0aab1dc708b7f89f4098b2397f81c9a96be8c79b2a06d10ac5940eae323f17236e4f343b0501b4d53d04bb510fa787e5b7298707ccc5e9a05b804ef0a1bfc96e875c79aa1566d7061dd95eebc5d605ce6fa2386bf57d36d4a10c7fa34ea4a113ffb93943f62696b25c9983a49b";
    private static final long JWT_EXPIRATION_MS = 3600000; // 1 hour

    public static String generateToken(String userId, String username, Role role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role.getAuthority());

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        System.out.println("Generated token: " + token); // Add logging
        return token;
    }
}