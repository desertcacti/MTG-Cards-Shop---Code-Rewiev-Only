package pl.desertcacti.mtgcardsshopsystem.service.order;

import org.springframework.http.ResponseEntity;
import pl.desertcacti.mtgcardsshopsystem.dto.OrderDto;
import pl.desertcacti.mtgcardsshopsystem.entity.Order;
import pl.desertcacti.mtgcardsshopsystem.response.ApiResponse;
import java.util.List;

/** OrderService interface provides method declarations for order-related operations. */
public interface OrderService {

    ResponseEntity<List<OrderDto>> getUserOrders();
    ResponseEntity<ApiResponse> processOrder(OrderDto orderDto);
    ResponseEntity<List<Order>> getOrdersByEmail(String email);
    ResponseEntity<ApiResponse> processGuestOrder(OrderDto orderDto);
}