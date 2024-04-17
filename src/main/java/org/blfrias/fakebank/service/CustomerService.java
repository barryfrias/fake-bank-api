package org.blfrias.fakebank.service;

import org.blfrias.fakebank.dto.CustomerDTO;

import java.util.Optional;

public interface CustomerService {
    CustomerDTO create(CustomerDTO customerDTO);
    Optional<CustomerDTO> getById(String id);
    boolean deleteById(String id);
    Object updateName(String customerId, String newName);

}