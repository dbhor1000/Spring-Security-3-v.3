package JC.Spring.Security3.service;

import java.time.LocalDateTime;

public interface TokenBlacklistService {

    public void addToBlacklist(String token, LocalDateTime expirationTime);

    public boolean isBlacklisted(String token);

    public void cleanUpExpiredTokens();
}