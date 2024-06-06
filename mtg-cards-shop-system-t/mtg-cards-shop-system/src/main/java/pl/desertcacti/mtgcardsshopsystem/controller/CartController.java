package pl.desertcacti.mtgcardsshopsystem.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.desertcacti.mtgcardsshopsystem.dto.CartItemDetail;
import pl.desertcacti.mtgcardsshopsystem.dto.CartItemDto;
import pl.desertcacti.mtgcardsshopsystem.dto.CustomerDetailsDto;
import pl.desertcacti.mtgcardsshopsystem.service.cart.CartService;
import java.util.List;

/** CartController class handles shopping cart requests. */
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /** Add Product to cart */
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartItemDto cartItem) {
        cartService.addToCart(cartItem);
        return ResponseEntity.ok("Product added to cart.");
    }

    /** Retrieves all cart items in DTO form. */
    @GetMapping
    public ResponseEntity<List<CartItemDetail>> getAllCartItems() {
        List<CartItemDetail> detailedItems = cartService.getAllItems();
        return ResponseEntity.ok(detailedItems);
    }

    /** Updates the quantity of a cart item based on productId */
    @PutMapping("/{id}")
    public ResponseEntity<CartItemDto> updateCartItemQuantity(@PathVariable Long id, @RequestBody CartItemDto cartItem) {
        CartItemDto updatedItem = cartService.updateItemQuantity(id, cartItem.getQuantity());
        return ResponseEntity.ok(updatedItem);
    }

    /** Deletes a cart item based on a productId*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    /** Updates customer details using form/DTO */
    @PostMapping("/customerDetails")
    public ResponseEntity<Void> updateCustomerDetails(@Valid @RequestBody CustomerDetailsDto customerDetails) {
        cartService.updateCustomerDetails(customerDetails);
        return ResponseEntity.ok().build();
    }

    /** Clears the cart after finalization of taking order process. */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok().build();
    }
}