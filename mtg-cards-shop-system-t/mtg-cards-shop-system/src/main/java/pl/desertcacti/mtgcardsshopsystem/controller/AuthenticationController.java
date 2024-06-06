package pl.desertcacti.mtgcardsshopsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.desertcacti.mtgcardsshopsystem.dto.LoginDto;
import pl.desertcacti.mtgcardsshopsystem.dto.RegisterDto;
import pl.desertcacti.mtgcardsshopsystem.service.auth.AuthenticationService;
import java.util.Map;

/** AuthenticationController class handles authentication-related requests
 such as login, logout, email user confirmation, resend email with confirmation and registration */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {

    /** Fields and Constructor with Autowired Fields */
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /** Log in a user using provided credentials. */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDto loginDto) {
        return authenticationService.loginUser(loginDto);
    }

    /** Log out the user by invalidating the JWT token. */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        return authenticationService.logout(request);
    }

    /** Confirm the user's email using the provided confirmation code. */
    @GetMapping("/confirm/{confirmationCode}")
    public ResponseEntity<Void> confirmUser(@PathVariable String confirmationCode) {
        return authenticationService.confirmUser(confirmationCode);
    }

    /** Resend the confirmation email to the user. */
    @PostMapping("/resendConfirmation")
    public ResponseEntity<Map<String, String>> resendConfirmationEmail(@RequestBody Map<String, String> payload) {
        return authenticationService.resendConfirmationEmail(payload);
    }

    /** Register a new user with provided data DTO. */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterDto registerDto) {
        return authenticationService.registerUser(registerDto);
    }
}
