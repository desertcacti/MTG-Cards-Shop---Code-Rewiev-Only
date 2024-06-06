package pl.desertcacti.mtgcardsshopsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.desertcacti.mtgcardsshopsystem.entity.CustomerEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailsDto {

    @NotBlank(message = "The name cannot be blank.")
    @Size(min = 2, message = "The name must contain at least 2 characters.")
    private String firstName;

    @NotBlank(message = "The name cannot be blank.")
    @Size(min = 3, message = "The name must contain at least 3 characters.")
    private String lastName;

    @NotBlank(message = "The postal code cannot be blank.")
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "The postal code must be in the format XX-XXX")
    private String postalCode;

    @NotBlank(message = "City cannot be blank.")
    @Size(min = 3, message = "The city name must contain at least 3 characters.")
    private String city;

    @Email(message = "Invalid email address format.")
    private String email;

    @NotBlank(message = "Street cannot be blank.")
    @Size(min = 3, message = "The street name must contain at least 3 characters.")
    private String street;

    @NotBlank(message = "Phone number cannot be blank.")
    @Pattern(regexp = "\\d{3}-\\d{3}-\\d{3}", message = "The phone number must contain exactly 9 digits in the format XXX-XXX-XXX.")
    private String phoneNumber;

    public CustomerDetailsDto(CustomerEntity customer) {
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.postalCode = customer.getPostalCode();
        this.city = customer.getCity();
        this.email = customer.getEmail();
        this.street = customer.getStreet();
        this.phoneNumber = customer.getPhoneNumber();
    }
}