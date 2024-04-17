package org.blfrias.fakebank.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.blfrias.fakebank.dto.CustomerDTO;
import org.blfrias.fakebank.mapper.CustomerMapper;
import org.blfrias.fakebank.model.Customer;
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

    @Override
    public boolean deleteById(String customerId) {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> updateName(String customerId, String newName) {
        if(StringUtils.isBlank(customerId)) {
            throw new IllegalArgumentException("Customer ID must not be blank");
        }

        if(StringUtils.isBlank(newName)) {
            throw new IllegalArgumentException("New name must not be blank");
        }

        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            return Optional.empty();
        }

        Customer customer = customerOptional.get();
        customer.setName(newName);

        Customer updatedCustomer = customerRepository.save(customer);
        return Optional.of(customerMapper.toDto(updatedCustomer));
    }

    }

