
package pl.desertcacti.mtgcardsshopsystem.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import pl.desertcacti.mtgcardsshopsystem.dto.LoginDto;
import pl.desertcacti.mtgcardsshopsystem.dto.RegisterDto;

import java.util.Map;

/** AuthenticationService interface defines methods for user authentication operations. */
public interface AuthenticationService {

    ResponseEntity<Map<String, String>> loginUser(LoginDto loginDto);
    ResponseEntity<Map<String, String>> logout(HttpServletRequest request);
    ResponseEntity<Void> confirmUser(String confirmationCode);
    ResponseEntity<Map<String, String>> resendConfirmationEmail(Map<String, String> payload);
    ResponseEntity<Map<String, String>> registerUser(RegisterDto registerDto);
}