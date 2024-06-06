package pl.desertcacti.mtgcardsshopsystem.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.desertcacti.mtgcardsshopsystem.entity.User;
import pl.desertcacti.mtgcardsshopsystem.repository.UserRepository;
import java.util.Date;
import java.util.stream.Collectors;

/** JwtTokenProvider class provides methods for generating and validating JWT tokens,
 * as well as extracting user information from the tokens. */
@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private final UserRepository userRepository;

    @Autowired
    public JwtTokenProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** generateToken()
    /*  Retrieve user details from the authentication object.
        Set token issue and expiry dates.
        Retrieve user authorities.
        Build and return the JWT token. */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(email)
                .claim("roles", authorities)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /** generateTokenForUser()
    /*  Validate the authentication principal.
        Retrieve the user by username or email.
        Check if the user's account is confirmed.
        Generate the JWT token.
        Validate the generated token.
        Return the bearer token. */
    public String generateTokenForUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new UsernameNotFoundException("Invalid login credentials.");
        }

        String username = ((UserDetails) principal).getUsername();
        User user = userRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid login credentials."));

        if (!user.getConfirmed()) {
            throw new LockedException("Click here to send verification email again.");
        }

        String token = "Bearer " + generateToken(authentication);
        if (!validateToken(token.substring(7), (UserDetails) authentication.getPrincipal())) {
            throw new IllegalArgumentException("Invalid token.");
        }

        return token;
    }
    /** extractUsername()
    /*  Parse the JWT token.
        Extract and return the username from the token claims. */
    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    /** validateToken()
    /*  Extract the username from the token.
        Compare the extracted username with the user details.
        Check if the token is expired.
         Return true if the token is valid, otherwise false. */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String emailFromToken = extractUsername(token);
            return emailFromToken.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /** isTokenExpired()
    /*  Parse the JWT token.
        Check the expiration date.
        Return true if the token is expired, otherwise false. */
    private boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }
}
