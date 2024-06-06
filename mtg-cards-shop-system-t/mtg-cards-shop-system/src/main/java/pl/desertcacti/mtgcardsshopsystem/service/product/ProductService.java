package pl.desertcacti.mtgcardsshopsystem.service.product;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import pl.desertcacti.mtgcardsshopsystem.dto.ProductDto;
import pl.desertcacti.mtgcardsshopsystem.entity.ProductEntity;
import java.util.List;

/** ProductService interface provides method declarations for product-related operations */
public interface ProductService {
    ResponseEntity<List<ProductDto>> getAllProducts();
    ResponseEntity<Resource> getProductImage(Long productId);
    String generateImageUrl(String fileId);
    ResponseEntity<List<ProductDto>> getProductsByCardType(String cardType);
    ProductDto getProduct(Long productId);
    ProductEntity getProductById(Long id);
}