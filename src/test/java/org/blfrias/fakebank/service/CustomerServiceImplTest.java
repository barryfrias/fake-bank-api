package org.blfrias.fakebank.service;

import org.blfrias.fakebank.dto.CustomerDTO;
import org.blfrias.fakebank.mapper.CustomerMapper;
import org.blfrias.fakebank.model.Customer;
import org.blfrias.fakebank.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        Customer customer = new Customer();
        when(customerMapper.toEntity(any())).thenReturn(customer);
        when(customerMapper.toDto(any())).thenReturn(customerDTO);
        when(customerRepository.save(any())).thenReturn(customer);

        CustomerDTO createdCustomer = customerService.create(customerDTO);

        assertEquals(customerDTO, createdCustomer);
        verify(customerRepository, times(1)).save(any());
    }

    @Test
    void testGetCustomerById() {
        CustomerDTO customerDTO = new CustomerDTO();
        Customer customer = new Customer();
        when(customerMapper.toDto(any())).thenReturn(customerDTO);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        Optional<CustomerDTO> retrievedCustomer = customerService.getById("123");

        assertEquals(customerDTO, retrievedCustomer.orElse(null));
        verify(customerRepository, times(1)).findById(any());
    }

    @Test
    void testDeleteCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        Customer customer = new Customer();
        when(customerMapper.toDto(any())).thenReturn(customerDTO);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));
        customerService.deleteById("123");
        verify(customerRepository, times(1)).deleteById(any());
    }

    @Test
    void testUpdateCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        Customer customer = new Customer();
        when(customerMapper.toDto(any())).thenReturn(customerDTO);
        when(customerRepository.findById(any())).thenReturn(Optional.of(customer));

        Optional<CustomerDTO> CustomerDTO = customerService.updateName("123", "New Name");
        assertEquals(customerDTO, CustomerDTO.orElse(null));

        verify(customerRepository, times(1)).findById(any());
        verify(customerMapper, times(1)).toDto(any());
        verify(customerRepository, times(1)).save(any());
    }












}