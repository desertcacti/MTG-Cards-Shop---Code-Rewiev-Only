package pl.desertcacti.mtgcardsshopsystem.service.user;

import org.springframework.http.ResponseEntity;
import pl.desertcacti.mtgcardsshopsystem.dto.RegisterDto;
import pl.desertcacti.mtgcardsshopsystem.entity.User;

/** UserService interface defines methods implementation for serviceImpl
 such as user registration, account confirmation, and finding users by email. */
public interface UserService {
    ResponseEntity<User> registerNewUserAccount(RegisterDto registerDto);
    ResponseEntity<String> confirmUser(String confirmationCode);
    ResponseEntity<User> findByEmail(String email);
}
