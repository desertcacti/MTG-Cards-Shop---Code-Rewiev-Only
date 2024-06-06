package pl.desertcacti.mtgcardsshopsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.desertcacti.mtgcardsshopsystem.entity.User;
import java.util.Optional;

/** UserRepository interface provides methods for performing CRUD operations
 * on User objects, including finding users by username, email, confirmation code,
 * or a combination of username and email. */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   @Query("SELECT u FROM User u WHERE u.username = :usernameOrEmail OR u.email = :usernameOrEmail")
   Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

   Optional<User> findByConfirmationCode(String confirmationCode);

   Optional<Object> findByUsername(String username);
   Optional<User> findByEmail(String email);

}

