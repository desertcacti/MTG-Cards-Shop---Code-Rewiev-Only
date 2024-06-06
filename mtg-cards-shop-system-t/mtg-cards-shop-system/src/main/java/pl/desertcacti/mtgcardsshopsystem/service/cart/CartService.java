package pl.desertcacti.mtgcardsshopsystem.service.cart;

import pl.desertcacti.mtgcardsshopsystem.dto.CartItemDetail;
import pl.desertcacti.mtgcardsshopsystem.dto.CartItemDto;
import pl.desertcacti.mtgcardsshopsystem.dto.CustomerDetailsDto;

import java.util.List;
/** CartService interface provides methods declaration for managing a shopping cart
 such as adding items to the cart, retrieving all items, updating the quantity of an item,
 deleting an item, and clearing the entire cart. */
public interface CartService {
    void addToCart(CartItemDto cartItem);
    List<CartItemDetail> getAllItems();
    CartItemDto updateItemQuantity(Long id, int quantity);
    void deleteItem(Long id);
    void clearCart();
    void updateCustomerDetails(CustomerDetailsDto customerDetailsDto);


}


