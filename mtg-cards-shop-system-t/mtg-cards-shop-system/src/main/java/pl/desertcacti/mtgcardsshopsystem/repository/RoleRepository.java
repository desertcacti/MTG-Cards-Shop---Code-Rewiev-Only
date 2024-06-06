package pl.desertcacti.mtgcardsshopsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.desertcacti.mtgcardsshopsystem.entity.Role;
import java.util.Optional;

/** RoleRepository interface provides methods for performing CRUD operations
 * on Role objects, including finding a role by its name. */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}