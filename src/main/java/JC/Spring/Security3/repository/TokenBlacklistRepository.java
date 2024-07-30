package JC.Spring.Security3.repository;

import JC.Spring.Security3.tokenManagement.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, String> {
    Optional<TokenBlacklist> findByToken(String token);
    void deleteByExpirationTimeBefore(LocalDateTime now);
}