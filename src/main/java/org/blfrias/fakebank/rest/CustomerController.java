package org.blfrias.fakebank.rest;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.blfrias.fakebank.dto.AccountDTO;
import org.blfrias.fakebank.dto.CustomerDTO;
import org.blfrias.fakebank.service.AccountService;
import org.blfrias.fakebank.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final AccountService accountService;


    @Operation(summary = "Create a new customer")
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody CustomerDTO customerDTO) {
        if(customerDTO.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id must be null");
        }
        CustomerDTO dto = customerService.create(customerDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Customer by id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        var customerDTO = customerService.getById(id);
        if (customerDTO.isPresent()) {
            return new ResponseEntity<>(customerDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get customer accounts")
    @GetMapping("/{customerId}/accounts")
    public List<AccountDTO> getCustomerAccounts(@PathVariable String customerId) {
        return accountService.getAllByCustomerId(customerId);
    }

    @Operation(summary = "Delete a customer by ID")
    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) {
        boolean deleted = customerService.deleteById(customerId);
        if (deleted) {
            return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Customer with ID " + customerId + " not found", HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/{customerId}/updateName")
    public ResponseEntity<?> updateCustomerName(@PathVariable String customerId, @RequestParam String newName) {
        if (StringUtils.isBlank(customerId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer ID must not be blank");
        }

        if (StringUtils.isBlank(newName)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New name must not be blank");
        }

        try {
            var updatedCustomer = customerService.updateName(customerId, newName);
            return ResponseEntity.ok(updatedCustomer);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
