package pl.desertcacti.mtgcardsshopsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.desertcacti.mtgcardsshopsystem.entity.CustomerEntity;

/** CustomerRepository interface provides methods for performing CRUD operations
 * on CustomerEntity objects. */
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
