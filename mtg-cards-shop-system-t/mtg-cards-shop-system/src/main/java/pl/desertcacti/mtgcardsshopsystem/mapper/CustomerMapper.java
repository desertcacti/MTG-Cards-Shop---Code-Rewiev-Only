package pl.desertcacti.mtgcardsshopsystem.mapper;

import org.springframework.stereotype.Component;
import pl.desertcacti.mtgcardsshopsystem.dto.CustomerDetailsDto;
import pl.desertcacti.mtgcardsshopsystem.entity.CustomerEntity;

/** CustomerMapper class provides methods for converting CustomerDetailsDto objects
 * to CustomerEntity objects. */
@Component
public class CustomerMapper {

    /** Convert a CustomerDetailsDto to a CustomerEntity. */
    public CustomerEntity convertDtoToEntity(CustomerDetailsDto dto) {
        CustomerEntity entity = new CustomerEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPostalCode(dto.getPostalCode());
        entity.setCity(dto.getCity());
        entity.setEmail(dto.getEmail());
        entity.setStreet(dto.getStreet());
        entity.setPhoneNumber(dto.getPhoneNumber());
        return entity;
    }
}