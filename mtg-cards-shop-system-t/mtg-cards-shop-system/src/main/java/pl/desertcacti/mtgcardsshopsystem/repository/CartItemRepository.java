package pl.desertcacti.mtgcardsshopsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.desertcacti.mtgcardsshopsystem.entity.CartItemEntity;
import pl.desertcacti.mtgcardsshopsystem.entity.ProductEntity;

import java.util.Optional;

/** CartItemRepository interface provides methods for performing CRUD operations
 * on CartItemEntity objects, including finding a cart item by its associated product. */
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    Optional<CartItemEntity> findByProduct(ProductEntity product);
}
