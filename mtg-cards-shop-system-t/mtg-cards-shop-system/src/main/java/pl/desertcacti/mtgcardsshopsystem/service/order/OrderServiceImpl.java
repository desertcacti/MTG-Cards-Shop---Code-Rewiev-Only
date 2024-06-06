package pl.desertcacti.mtgcardsshopsystem.service.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.desertcacti.mtgcardsshopsystem.dto.OrderDto;
import pl.desertcacti.mtgcardsshopsystem.entity.*;
import pl.desertcacti.mtgcardsshopsystem.mapper.OrderMapper;
import pl.desertcacti.mtgcardsshopsystem.repository.OrderRepository;
import pl.desertcacti.mtgcardsshopsystem.repository.UserRepository;
import pl.desertcacti.mtgcardsshopsystem.response.ApiResponse;
import java.util.List;
import java.util.stream.Collectors;

/** OrderServiceImpl class handling the implementation logic for order-related operations
 such as retrieving user orders by email, and processing orders for authenticated and guest users */
@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
        private final OrderRepository orderRepository;
        private final UserRepository userRepository;
        private final OrderMapper orderMapper;
        @Autowired
        public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, OrderMapper orderMapper) {
                this.orderRepository = orderRepository;
                this.userRepository = userRepository;
                this.orderMapper = orderMapper;
        }

        /** getUserOrders()
         Method checking if User is Authenticated or not.
         Retrieving email from token and create list of orders retrieved by email from orderRepository.
         Convert orders to OrderDto and return a ResponseEntity with status OK mapped list of orders. */
        @Override
        public ResponseEntity<List<OrderDto>> getUserOrders() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated()) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }

                String email = authentication.getName();
                List<Order> orders = orderRepository.findByUserEmail(email);

                List<OrderDto> orderDto = orders.stream().map(OrderDto::new).collect(Collectors.toList());

                if (orderDto.isEmpty()) {
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(orderDto);
        }

        /** processOrder()
         Determine if the order is for a registered user or a guest.
         Delegate to the appropriate method for processing the order. */
        @Override
        public ResponseEntity<ApiResponse> processOrder(OrderDto orderDto) {
                String userEmail = orderDto.getCustomerDetails().getEmail();
                return (userEmail != null && !userEmail.isEmpty())
                        ? processOrderForRegisteredUser(orderDto, userEmail)
                        : processGuestOrder(orderDto);
        }


        /** getOrdersByEmail()
         Retrieve orders list by the provided email.
         Return a ResponseEntity containing the list of orders. */
        @Override
        public ResponseEntity<List<Order>> getOrdersByEmail(String email) {
                List<Order> orders = orderRepository.findByEmail(email);
                if (orders.isEmpty()) {
                        return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(orders);
        }

        /** processGuestOrder()
         Process an order for a guest user.
         Return a ResponseEntity with the result of the guest order processing. */
        @Override
        public ResponseEntity<ApiResponse> processGuestOrder(OrderDto orderDto) {
                return processOrder(orderDto, null);
        }

        /** processOrderForRegisteredUser()
         Find the user by email.
         If found, process the order for register user.
         If not, return NOT FOUND response. */
        private ResponseEntity<ApiResponse> processOrderForRegisteredUser(OrderDto orderDto, String email) {
                return userRepository.findByEmail(email)
                        .map(user -> processOrder(orderDto, user))
                        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "User not found with email: " + email)));
        }


        /** processOrder()
         Convert OrderDto to OrderEntity and save order in repository.
         Log the successful processing and return an OK response.
         If an exception occurs, log the error and return an INTERNAL SERVER ERROR response. */
        private ResponseEntity<ApiResponse> processOrder(OrderDto orderDto, User user) {
                try {
                        Order order = orderMapper.convertDtoToEntity(orderDto, user);
                        orderRepository.save(order);
                        log.info("Order processed successfully. Order ID: {}", order.getId());
                        return ResponseEntity.ok(new ApiResponse(true, "Order processed successfully."));
                } catch (Exception e) {
                        log.error("Failed to process order: {}", e.getMessage(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Order processing failed due to: " + e.getMessage()));
                }
        }
}