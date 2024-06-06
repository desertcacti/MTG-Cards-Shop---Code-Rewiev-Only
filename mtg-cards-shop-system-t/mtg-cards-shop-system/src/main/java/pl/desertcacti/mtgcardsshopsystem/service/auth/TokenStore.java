package pl.desertcacti.mtgcardsshopsystem.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

/** TokenStore class manages the invalidation and validation of JWT tokens. */
@Slf4j
@Service
public class TokenStore {

    @Value("${app.jwtSecret}")
    private String secretKey;

    private final ConcurrentHashMap<String, Long> tokenBlacklist = new ConcurrentHashMap<>();

    /** invalidateToken()
     /*  Invalidates a token by adding it to the blacklist with its expiry time. */
    public void invalidateToken(String token) {
        long expiryTime = decodeTokenToGetExpiry(token);
        tokenBlacklist.put(token, expiryTime);
    }

    /** isTokenInvalidated()
     /*  Checks if a token has been invalidated. */
    public boolean isTokenInvalidated(String token) {
        return tokenBlacklist.containsKey(token) && tokenBlacklist.get(token) > System.currentTimeMillis();
    }

    /** decodeTokenToGetExpiry()
     /*  Decodes a token to extract its expiry time. */
    public long decodeTokenToGetExpiry(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().getTime();
        } catch (Exception e) {
            log.error("Failed to decode token: {}", e.getMessage());
            return EXPIRED_TOKEN_TIME;
        }
    }
    private static final long EXPIRED_TOKEN_TIME = 0L;
}
