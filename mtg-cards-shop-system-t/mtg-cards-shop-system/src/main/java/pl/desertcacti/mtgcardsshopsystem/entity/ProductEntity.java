package pl.desertcacti.mtgcardsshopsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

/**
 * ProductEntity represents a product in the system.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Unique identifier for the product

    @Column(nullable = false)
    private String name;  // Name of the product

    @Column(nullable = false)
    private double price;  // Price of the product

    private String fileId;  // Identifier for the associated file (e.g., image or document)

    private String cardType;  // Type of card associated with the product (e.g., gift card, loyalty card)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return Double.compare(that.price, price) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(fileId, that.fileId) &&
                Objects.equals(cardType, that.cardType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, fileId, cardType);
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", fileId='" + fileId + '\'' +
                ", cardType='" + cardType + '\'' +
                '}';
    }
}