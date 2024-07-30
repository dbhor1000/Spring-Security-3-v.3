package JC.Spring.Security3.service;

import JC.Spring.Security3.repository.TokenBlacklistRepository;
import JC.Spring.Security3.tokenManagement.TokenBlacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    @Autowired
    private TokenBlacklistRepository tokenBlacklistRepository;

    public void addToBlacklist(String token, LocalDateTime expirationTime) {
        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        tokenBlacklist.setRevoked(true);
        tokenBlacklist.setExpirationTime(expirationTime);
        tokenBlacklistRepository.save(tokenBlacklist);
    }

    public boolean isBlacklisted(String token) {
        return tokenBlacklistRepository.findByToken(token)
                .map(TokenBlacklist::isRevoked)
                .orElse(false);
    }

    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanUpExpiredTokens() {
        tokenBlacklistRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }
}