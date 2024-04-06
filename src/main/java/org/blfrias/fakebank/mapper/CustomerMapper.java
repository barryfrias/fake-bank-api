package org.blfrias.fakebank.mapper;

import org.blfrias.fakebank.dto.CustomerDTO;
import org.blfrias.fakebank.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerDTO dto);
    CustomerDTO toDto(Customer entity);
}