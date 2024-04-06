package org.blfrias.fakebank.service;

import org.blfrias.fakebank.dto.CustomerDTO;

import java.util.Optional;

public interface CustomerService {
    CustomerDTO create(CustomerDTO customerDTO);
    Optional<CustomerDTO> getById(String id);
}