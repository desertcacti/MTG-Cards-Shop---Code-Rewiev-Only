package pl.desertcacti.mtgcardsshopsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.desertcacti.mtgcardsshopsystem.entity.ProductEntity;
import java.util.List;

/** ProductRepository interface provides methods for performing CRUD operations
 * on ProductEntity objects, including finding products by card type. */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByCardType(String cardType);

}
