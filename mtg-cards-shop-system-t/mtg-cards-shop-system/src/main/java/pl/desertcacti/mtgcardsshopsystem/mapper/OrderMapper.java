package pl.desertcacti.mtgcardsshopsystem.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.desertcacti.mtgcardsshopsystem.dto.OrderDto;
import pl.desertcacti.mtgcardsshopsystem.entity.Order;
import pl.desertcacti.mtgcardsshopsystem.entity.OrderItem;
import pl.desertcacti.mtgcardsshopsystem.entity.User;

import java.util.List;
import java.util.stream.Collectors;

/** OrderMapper class provides methods for converting OrderDto objects to Order entities. */
@Component
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;
    private final CustomerMapper customerMapper;

    @Autowired
    public OrderMapper(OrderItemMapper orderItemMapper, CustomerMapper customerMapper) {
        this.orderItemMapper = orderItemMapper;
        this.customerMapper = customerMapper;
    }

    /** Convert an OrderDto to an Order entity. */
    public Order convertDtoToEntity(OrderDto orderDto, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setEmail(orderDto.getEmail());

        if (orderDto.getCustomerDetails() != null) {
            order.setCustomer(customerMapper.convertDtoToEntity(orderDto.getCustomerDetails()));
        }

        List<OrderItem> orderItems = orderDto.getCartItems().stream()
                .map(itemDto -> orderItemMapper.convertDtoToEntity(itemDto, order))
                .collect(Collectors.toList());
        order.setItems(orderItems);

        return order;
    }
}