package pl.desertcacti.mtgcardsshopsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartItemDto {
    private Long productId;
    private int quantity;
}