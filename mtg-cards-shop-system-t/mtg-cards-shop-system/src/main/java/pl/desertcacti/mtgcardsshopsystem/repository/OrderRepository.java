package pl.desertcacti.mtgcardsshopsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.desertcacti.mtgcardsshopsystem.entity.Order;
import java.util.List;

/** OrderRepository interface provides methods for performing CRUD operations
 * on Order objects, including finding orders by email, user email, and user ID. */
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByEmail(String email);
    List<Order> findByUserEmail(String email);
    List<Order> findAllByUserId(Long userId);

}