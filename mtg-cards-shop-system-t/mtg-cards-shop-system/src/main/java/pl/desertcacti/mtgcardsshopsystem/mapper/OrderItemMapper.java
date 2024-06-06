package pl.desertcacti.mtgcardsshopsystem.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.desertcacti.mtgcardsshopsystem.dto.CartItemDetail;
import pl.desertcacti.mtgcardsshopsystem.entity.Order;
import pl.desertcacti.mtgcardsshopsystem.entity.OrderItem;
import pl.desertcacti.mtgcardsshopsystem.entity.ProductEntity;
import pl.desertcacti.mtgcardsshopsystem.repository.ProductRepository;

/** OrderItemMapper class provides methods for converting CartItemDetail objects to OrderItem entities. */
@Component
public class OrderItemMapper {
    private final ProductRepository productRepository;

    @Autowired
    public OrderItemMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    /** Convert a CartItemDetail to an OrderItem entity. */
    public OrderItem convertDtoToEntity(CartItemDetail itemDto, Order order) {
        ProductEntity productEntity = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + itemDto.getProductId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(productEntity);
        orderItem.setQuantity(itemDto.getQuantity());
        orderItem.setPrice(productEntity.getPrice() * itemDto.getQuantity());

        return orderItem;
    }
}