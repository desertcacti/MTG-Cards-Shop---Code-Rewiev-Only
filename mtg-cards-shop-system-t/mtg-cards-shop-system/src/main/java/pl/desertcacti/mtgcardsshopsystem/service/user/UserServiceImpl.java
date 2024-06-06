package pl.desertcacti.mtgcardsshopsystem.service.user;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.desertcacti.mtgcardsshopsystem.dto.RegisterDto;
import pl.desertcacti.mtgcardsshopsystem.entity.User;
import pl.desertcacti.mtgcardsshopsystem.repository.RoleRepository;
import pl.desertcacti.mtgcardsshopsystem.repository.UserRepository;
import pl.desertcacti.mtgcardsshopsystem.service.auth.EmailService;
import java.util.Set;
import java.util.UUID;

/** UserServiceImpl class provides implementations for user-related operations
 such as registering a new user, confirming user accounts, and finding users by email. */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /** registerNewUserAccount()
     /*  Registers a new user account with the provided registration details.
     Checks if the user already exists and sends a confirmation email if the registration is successful.
     Returns a ResponseEntity with the created user or an error status. */
    @Transactional
    @Override
    public ResponseEntity<User> registerNewUserAccount(RegisterDto registerDto) {
        if (userExists(registerDto.getEmail()) || userExists(registerDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        User user = createUser(registerDto);
        userRepository.save(user);

        if (!sendConfirmationEmail(user)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /** createUser()
     /*  Creates a new User entity based on the provided registration details.
     Returns the created User entity. */
    private User createUser(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setEmail(registerDto.getEmail());
        user.setRoles(Set.of(roleRepository.findByName("USER").orElseThrow(() -> new IllegalArgumentException("Role USER not found"))));
        user.setConfirmed(false);
        user.setConfirmationCode(UUID.randomUUID().toString());
        return user;
    }
    /** sendConfirmationEmail()
     /*  Sends a confirmation email to the user with a confirmation link.
     Returns true if the email was sent successfully, false otherwise. */
    private boolean sendConfirmationEmail(User user) {
        try {
            String confirmationLink = generateConfirmationLink(user.getConfirmationCode());
            emailService.sendConfirmationEmail(user.getEmail(), confirmationLink);
            log.info("Verification email sent to: " + user.getEmail());
            return true;
        } catch (Exception e) {
            log.error("Error during email sending", e);
            return false;
        }
    }

    /** confirmUser()
     /*  Confirms the user's account using the provided confirmation code.
     Returns a ResponseEntity with a success or error message. */
    @Override
    public ResponseEntity<String> confirmUser(String confirmationCode) {
        return userRepository.findByConfirmationCode(confirmationCode)
                .map(user -> {
                    if (!user.getConfirmed()) {
                        user.setConfirmed(true);
                        userRepository.save(user);
                    }
                    return ResponseEntity.ok("User confirmed successfully");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid confirmation code"));
    }

    /** findByEmail()
     /*  Finds a user by their email address.
     Returns a ResponseEntity with the found user or an error status. */
    @Override
    public ResponseEntity<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /** userExists()
     /*  Checks if a user exists by their username or email address.
     Returns true if the user exists, false otherwise. */
    private boolean userExists(String identifier) {
        return userRepository.findByUsernameOrEmail(identifier).isPresent();
    }


    /** generateConfirmationLink()
     /*  Generates a confirmation link using the provided confirmation code.
     Returns the generated confirmation link. */
    private String generateConfirmationLink(String confirmationCode) {
        return "http://localhost:8080/api/auth/confirm/" + confirmationCode;
    }
}

