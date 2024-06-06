package pl.desertcacti.mtgcardsshopsystem.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.desertcacti.mtgcardsshopsystem.dto.OrderDto;
import pl.desertcacti.mtgcardsshopsystem.entity.Order;
import pl.desertcacti.mtgcardsshopsystem.response.ApiResponse;
import pl.desertcacti.mtgcardsshopsystem.service.order.OrderService;
import java.util.List;

/** OrderController class handles order process requests
 such as retrieving user orders, retrieving orders by email,
 processing orders for authenticated users and guests */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    /** Fields and Constructor with Autowired Fields */
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** Retrieving orders for registered and actually logged User. */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders() {
        return orderService.getUserOrders();
    }

    /** Process order for authenticated User. */
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse> processOrder(@Valid @RequestBody OrderDto orderDto) {
        return orderService.processOrder(orderDto);
    }

    /** Get orders list by email used to finalize the order */
    @GetMapping("/by-email")
    public ResponseEntity<List<Order>> getOrdersByEmail(@RequestParam String email) {
        return orderService.getOrdersByEmail(email);
    }

    /** Process order for guest */
    @PostMapping("/guest-checkout")
    public ResponseEntity<ApiResponse> processGuestOrder(@RequestBody OrderDto orderDto) {
        return orderService.processGuestOrder(orderDto);
    }
}