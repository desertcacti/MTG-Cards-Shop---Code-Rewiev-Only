package pl.desertcacti.mtgcardsshopsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.desertcacti.mtgcardsshopsystem.entity.Order;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private String email;
    private List<CartItemDetail> cartItems;
    private CustomerDetailsDto customerDetails;
    public OrderDto(Order order) {
        this.email = order.getEmail();
        this.cartItems = order.getItems().stream()
                .map(item -> new CartItemDetail(item))
                .collect(Collectors.toList());
        this.customerDetails = new CustomerDetailsDto(order.getCustomer());
    }
}
