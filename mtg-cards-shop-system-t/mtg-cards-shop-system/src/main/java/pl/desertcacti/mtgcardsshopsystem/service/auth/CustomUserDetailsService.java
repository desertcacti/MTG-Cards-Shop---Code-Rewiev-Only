package pl.desertcacti.mtgcardsshopsystem.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.desertcacti.mtgcardsshopsystem.entity.User;
import pl.desertcacti.mtgcardsshopsystem.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

/** CustomUserDetailsService class implements UserDetailsService
 interface to provide custom user authentication logic. */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** loadUserByUsername()
     /*  Loads the user based on username or email.*/
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return userRepository.findByUsernameOrEmail(usernameOrEmail)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
    }

    /** createSpringSecurityUser()
     /*  Creates a UserDetails object from the UserEntity. */
    private UserDetails createSpringSecurityUser(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // Use email as the username
                user.getPassword(),
                getAuthorities(user)
        );
    }

    /** getAuthorities()
     /*  Converts the roles of the user to a collection of GrantedAuthority */
    private static Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
