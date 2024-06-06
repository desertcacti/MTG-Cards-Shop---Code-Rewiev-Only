package pl.desertcacti.mtgcardsshopsystem.service.cart;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.desertcacti.mtgcardsshopsystem.dto.CartItemDetail;
import pl.desertcacti.mtgcardsshopsystem.dto.CartItemDto;
import pl.desertcacti.mtgcardsshopsystem.dto.CustomerDetailsDto;
import pl.desertcacti.mtgcardsshopsystem.dto.ProductDto;
import pl.desertcacti.mtgcardsshopsystem.entity.CartItemEntity;
import pl.desertcacti.mtgcardsshopsystem.entity.CustomerEntity;
import pl.desertcacti.mtgcardsshopsystem.entity.ProductEntity;
import pl.desertcacti.mtgcardsshopsystem.repository.CartItemRepository;
import pl.desertcacti.mtgcardsshopsystem.repository.CustomerRepository;
import pl.desertcacti.mtgcardsshopsystem.service.product.ProductService;
import java.util.List;
import java.util.stream.Collectors;

/** CartServiceImpl class provides implementations for cart operations
 such as adding items to the cart, retrieving all items, updating item quantity,
 deleting items, and clearing the cart. */
@Service
@Transactional
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    private final CustomerRepository customerRepository;

    @Autowired
    public CartServiceImpl(CartItemRepository cartItemRepository, ProductService productService, CustomerRepository customerRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.customerRepository = customerRepository;
    }

    /** addToCart()
     /*  Adds a product to the cart.
     If the product already exists in the cart,
     its quantity is updated; otherwise, a new cart item is created. */
    @Override
    public void addToCart(CartItemDto cartItemDto) {
        ProductEntity product = productService.getProductById(cartItemDto.getProductId());
        CartItemEntity cartItem = cartItemRepository.findByProduct(product)
                .map(item -> updateCartItemQuantity(item, cartItemDto.getQuantity()))
                .orElseGet(() -> createNewCartItem(product, cartItemDto.getQuantity()));
        cartItemRepository.save(cartItem);
    }

    /** createNewCartItem()
     /*  Creates and returns a new cart item with the specified product and quantity. */
    private CartItemEntity createNewCartItem(ProductEntity product, int quantity) {
        CartItemEntity cartItem = new CartItemEntity();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        return cartItem;
    }

    /** updateCartItemQuantity()
     /*  Updates the quantity of an existing cartItem and returns this cartItem. */
    private CartItemEntity updateCartItemQuantity(CartItemEntity cartItem, int quantity) {
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        return cartItem;
    }

    /** getAllItems()
     /*  Retrieves all items in the cart, converts and returns them as a list of CartItemDetail. */
    @Override
    public List<CartItemDetail> getAllItems() {
        return cartItemRepository.findAll().stream()
                .map(this::convertToCartItemDetail)
                .collect(Collectors.toList());
    }

    /** convertToCartItemDetail()
     /*  Converts a CartItemEntity to a CartItemDetail. */
    private CartItemDetail convertToCartItemDetail(CartItemEntity cartItemEntity) {
        ProductDto product = productService.getProduct(cartItemEntity.getProduct().getId());
        String imageUri = "/api/products/" + product.getId() + "/image";
        return new CartItemDetail(
                cartItemEntity.getId(),
                product.getId(),
                product.getName(),
                cartItemEntity.getQuantity(),
                product.getPrice(),
                imageUri
        );
    }

    /** updateItemQuantity()
     /*  Updates specific item quantity and returns new updated CartItem. */
    @Override
    public CartItemDto updateItemQuantity(Long id, int quantity) {
        CartItemEntity item = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + id));
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return new CartItemDto(item.getProduct().getId(), item.getQuantity());
    }

    /** deleteItem()
     /*  Deletes a specific item from the cart. */
    @Override
    public void deleteItem(Long id) {
        if (!cartItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item not found with id: " + id);
        }
        cartItemRepository.deleteById(id);
    }

    /** clearCart()
     /*  Clears all items from the cart. Used to delete all items from cart after proper order finalization. */
    @Override
    public void clearCart() {
        cartItemRepository.deleteAll();
    }


    @Override
    public void updateCustomerDetails(CustomerDetailsDto customerDetailsDto) {
        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName(customerDetailsDto.getFirstName());
        customer.setLastName(customerDetailsDto.getLastName());
        customer.setPostalCode(customerDetailsDto.getPostalCode());
        customer.setCity(customerDetailsDto.getCity());
        customer.setEmail(customerDetailsDto.getEmail());
        customer.setStreet(customerDetailsDto.getStreet());
        customer.setPhoneNumber(customerDetailsDto.getPhoneNumber());

        customerRepository.save(customer);
    }

}

