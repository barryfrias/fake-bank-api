package org.blfrias.fakebank.service;

import lombok.RequiredArgsConstructor;
import org.blfrias.fakebank.dto.CustomerDTO;
import org.blfrias.fakebank.mapper.CustomerMapper;
import org.blfrias.fakebank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerDTO create(CustomerDTO customerDTO) {
        return customerMapper.toDto(customerRepository.save(customerMapper.toEntity(customerDTO)));
    }

    @Override
    public Optional<CustomerDTO> getById(String id) {
        return customerRepository.findById(id).map(customerMapper::toDto);
    }
}