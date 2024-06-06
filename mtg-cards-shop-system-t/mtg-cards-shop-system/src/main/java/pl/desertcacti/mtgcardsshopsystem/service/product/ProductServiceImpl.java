package pl.desertcacti.mtgcardsshopsystem.service.product;

import com.google.api.services.drive.Drive;
import org.springframework.core.io.Resource;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import pl.desertcacti.mtgcardsshopsystem.dto.ProductDto;
import pl.desertcacti.mtgcardsshopsystem.entity.ProductEntity;
import pl.desertcacti.mtgcardsshopsystem.repository.ProductRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.client.RestTemplate;

/** ProductServiceImpl class provides the implementation of the ProductService interface,
 such as retrieving all products retrieving product images, generating image URLs,
 retrieving products by card type, and retrieving products by ID. */
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Drive drive;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, Drive drive) {
        this.productRepository = Objects.requireNonNull(productRepository, "productRepository must not be null");
        this.drive = Objects.requireNonNull(drive, "drive must not be null");
    }

    /** getAllProductsResponse()
    /*  Retrieve all product entities from the repository.
     Convert product entities to Product objects.
     Return a ResponseEntity containing a list of all products. */
    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        List<ProductDto> products = productEntities.stream()
                .map(this::convertToProduct)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    /** getProductImageResponse()
     /*  Method road map:
     *1* Retrieve the product entity by ID.
     *2* Generate the image URL using the file ID.
     *3* Retrieve the image as a byte array from the URL.
     *4* Return the image as a ResponseEntity.  */
    @Override
    public ResponseEntity<Resource> getProductImage(Long productId) {
        try {
            // *1*
            ProductEntity productEntity = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));
            String fileId = productEntity.getFileId();
            if (fileId == null || fileId.isEmpty()) {
                throw new RuntimeException("File ID is not available for product: " + productId);
            }
            // *2*
            String imageUrl = generateImageUrl(fileId);
            if (imageUrl == null || imageUrl.isEmpty()) {
                throw new RuntimeException("Image URL is not available for product: " + productId);
            }
            try {
                // *3*
                RestTemplate restTemplate = new RestTemplate();
                byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);

                // *4*
                HttpHeaders headers = new HttpHeaders();
                headers.setCacheControl(CacheControl.noCache().getHeaderValue());
                headers.setContentType(MediaType.IMAGE_PNG);

                return new ResponseEntity<>(new ByteArrayResource(imageBytes), headers, HttpStatus.OK);
            } catch (Exception e) {
                throw new RuntimeException("Error while retrieving image for product: " + productId, e);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /** generateImageUrl()
     /*  Generate the URL for a product image. */
    @Override
    public String generateImageUrl(String fileId) {
        return "https://drive.google.com/uc?export=view&id=" + fileId;
    }

    /** getProductsByCardTypeResponse()
     /*  Retrieve product entities by card type from the repository.
     Convert product entities to Product objects.
     Return a ResponseEntity containing a list of products by card type. */
    @Override
    public ResponseEntity<List<ProductDto>> getProductsByCardType(String cardType) {
        List<ProductEntity> productEntities = productRepository.findByCardType(cardType);
        List<ProductDto> products = productEntities.stream()
                .map(this::convertToProduct)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    /** getProduct()
     /*  Retrieve the product entity by ID.
     Convert the product entity to a Product object.
     Return the Product object.  */
    @Override
    public ProductDto getProduct(Long productId) {
        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        return convertToProduct(productEntity);
    }

    /** convertToProduct()
     /*  Convert a ProductEntity to a Product object. */
    private ProductDto convertToProduct(ProductEntity productEntity) {
        return new ProductDto(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice(),
                productEntity.getCardType()
        );
    }

    /** getProductById()
     /*  Retrieve a product entity by its ID. */
    @Override
    public ProductEntity getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + id));
    }
}







