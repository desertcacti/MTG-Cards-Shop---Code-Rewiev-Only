package pl.desertcacti.mtgcardsshopsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.desertcacti.mtgcardsshopsystem.dto.ProductDto;
import pl.desertcacti.mtgcardsshopsystem.service.product.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;

/** ProductController class handles requests related to products
 such as retrieving all products, product images,  products by card type */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    /** Fields and Constructor with Autowired Fields */
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /** Retrieving all products. */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return productService.getAllProducts();
    }

    /** Retrieving the image of a specific product. */
    @GetMapping("/{productId}/image")
    public ResponseEntity<Resource> getProductImage(@PathVariable Long productId) {
        return productService.getProductImage(productId);
    }

    /** Retrieving products by card type. */
    @GetMapping("/type/{cardType}")
    public ResponseEntity<List<ProductDto>> getProductsByCardType(@PathVariable String cardType) {
        return productService.getProductsByCardType(cardType);
    }
}