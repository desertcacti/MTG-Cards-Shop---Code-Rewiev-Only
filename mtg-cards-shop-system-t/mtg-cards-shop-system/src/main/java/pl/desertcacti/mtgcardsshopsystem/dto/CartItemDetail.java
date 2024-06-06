package pl.desertcacti.mtgcardsshopsystem.dto;

import lombok.*;
import pl.desertcacti.mtgcardsshopsystem.entity.OrderItem;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDetail {
        private Long id;
        private Long productId;
        private String name;
        private int quantity;
        private double price;
        private String imageUrl;

        public CartItemDetail(OrderItem item) {
                this.id = item.getId();
                this.productId = item.getProduct().getId();
                this.name = item.getProduct().getName();
                this.quantity = item.getQuantity();
                this.price = item.getPrice();

        }
}