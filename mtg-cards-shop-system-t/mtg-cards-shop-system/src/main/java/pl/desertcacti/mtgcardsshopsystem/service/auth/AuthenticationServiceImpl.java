package pl.desertcacti.mtgcardsshopsystem.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.desertcacti.mtgcardsshopsystem.dto.LoginDto;
import pl.desertcacti.mtgcardsshopsystem.dto.RegisterDto;
import pl.desertcacti.mtgcardsshopsystem.entity.User;
import pl.desertcacti.mtgcardsshopsystem.repository.UserRepository;
import pl.desertcacti.mtgcardsshopsystem.security.JwtTokenProvider;
import pl.desertcacti.mtgcardsshopsystem.service.user.UserService;
import java.net.URI;
import java.util.Collections;
import java.util.Map;


/** AuthenticationServiceImpl class provides implementations for user authentication operations
 such as login, logout, user confirmation, resending confirmation emails, and user registration. */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenStore tokenStore;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserService userService, UserRepository userRepository, EmailService emailService, JwtTokenProvider jwtTokenProvider, TokenStore tokenStore) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenStore = tokenStore;
    }

    /** loginUser()
     /*  Logs information about the process steps.
     Authenticates the user with LoginDto credentials.
     Generates and returns a JWT token in the "Authorization" header. */
    @Override
    public ResponseEntity<Map<String, String>> loginUser(LoginDto loginDto) {
        log.info("Attempting to log in user: {}", loginDto.getUsernameOrEmail());
        Authentication authentication = authenticateUser(loginDto);
        String token = jwtTokenProvider.generateTokenForUser(authentication);
        log.info("User logged in successfully: {}", loginDto.getUsernameOrEmail());
        return ResponseEntity.ok(Collections.singletonMap("Authorization", token));
    }

    /** logout()
     /*  Method road map:
     *1* Extract the token from the "Authorization" header in the request.
     *2* Check if the token is present and starts with "Bearer ".
     *3* If valid, invalidate the token using the token store.
     *4* Return a success message in the response.
     *5* If the token is not provided or invalid, return Http status and error message. */
    @Override
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        // *1*
        String authHeader = request.getHeader("Authorization");
        // *2*
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // *3*
            String token = authHeader.substring(7);
            tokenStore.invalidateToken(token);
            // *4*
            return ResponseEntity.ok(Collections.singletonMap("message", "User logged out successfully"));
        }
        // *5*
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "No token provided"));
    }

    /** confirmUser()
     /*  Method for confirming User's account using the provided confirmation code and redirects accordingly. */
    @Override
    public ResponseEntity<Void> confirmUser(String confirmationCode) {
        boolean isConfirmed = userService.confirmUser(confirmationCode).hasBody();
        String redirectUrl = isConfirmed ? "http://localhost:3000/?confirmation=success" : "http://localhost:3000/?confirmation=failed";
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
    }

    /** resendConfirmationEmail()
     /*  Resends a confirmation email to the user based on the provided usernameOrEmail.
     Returns status OK with a success message or UsernameNotFoundException with an error message */
    @Override
    public ResponseEntity<Map<String, String>> resendConfirmationEmail(Map<String, String> payload) {
        String usernameOrEmail = payload.get("usernameOrEmail");
        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                .map(user -> {
                    sendConfirmationEmail(user);
                    return ResponseEntity.ok(Collections.singletonMap("message", "Confirmation email resent successfully."));
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
    }

    /** registerUser()
     /*  Registers a new User with the provided RegisterDto details.
     Returns a response with a success message and the email address to which the verification email was sent. */
    @Override
    public ResponseEntity<Map<String, String>> registerUser(RegisterDto registerDto) {
        User newUser = userService.registerNewUserAccount(registerDto).getBody();
        return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully. A verification email has been sent to: " + newUser.getEmail()));
    }

    /** authenticateUser()
     /*  Authenticates the user using the provided loginDto credentials.
     Returns authenticated Object */
    private Authentication authenticateUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /** sendConfirmationEmail()
     /*  Constructs a confirmation link using the User's confirmation code
     and sends it to the user's email address using the `EmailService`. */
    private void sendConfirmationEmail(User user) {
        String basePath = "http://localhost:8080/api/auth/confirm";
        String confirmationLink = basePath + "/" + user.getConfirmationCode();
        emailService.sendConfirmationEmail(user.getEmail(), confirmationLink);
    }
}