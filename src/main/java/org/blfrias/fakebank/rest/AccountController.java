package org.blfrias.fakebank.rest;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.blfrias.fakebank.dto.AccountDTO;
import org.blfrias.fakebank.dto.TransactionDTO;
import org.blfrias.fakebank.dto.TransferDTO;
import org.blfrias.fakebank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Create a new account")
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO) {
        if (StringUtils.isBlank(accountDTO.getCustomerId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Customer Id must not be blank");
        }

        if (accountDTO.getBalance() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Balance must not be less than 0");
        }

        var dto = accountService.create(accountDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get account by accountNumber")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getByAccountNumber(@PathVariable String accountNumber) {
        var accountDTO = accountService.getByAccountNumber(accountNumber);
        if (accountDTO.isPresent()) {
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Transfer balance between accounts")
    @PostMapping("/transfer")
    public ResponseEntity<?> transferBalance(@RequestBody TransferDTO transferDTO) {
        if (StringUtils.isBlank(transferDTO.getSourceAccountNumber()) || StringUtils.isBlank(transferDTO.getTargetAccountNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Source and target account numbers must not be blank");
        }

        if (transferDTO.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Amount must be greater than 0");
        }

        if (!accountService.isValidAccount(transferDTO.getSourceAccountNumber())
                || !accountService.isValidAccount(transferDTO.getTargetAccountNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid source or target account");
        }

        try {
            var txnDTO = accountService.transferBalance(
                    transferDTO.getSourceAccountNumber(), transferDTO.getTargetAccountNumber(), transferDTO.getAmount()
            );
            return ResponseEntity.ok(txnDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Get transaction history for an account")
    @GetMapping("/{accountNumber}/transactions")
    public List<TransactionDTO> getTransactionHistory(@PathVariable String accountNumber) {
        return accountService.getTransactionHistory(accountNumber);
    }

    @Operation(summary = "Delete a customer by ID")
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<?> deleteByAccountNumber(@PathVariable String accountNumber) {
        boolean deleted = accountService.deleteByAccountNumber(accountNumber);
        if (deleted) {
            return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account with account Number " + accountNumber + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable String accountNumber, @RequestParam(name = "newAmount", required = false) Double newAmount) {
        if (newAmount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New amount must be provided");
        }
        if (newAmount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New deposit amount must be greater than zero");
        }
        AccountDTO accountDTO = accountService.deposit(accountNumber, newAmount);

        return ResponseEntity.ok(accountDTO);
    }



    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable String accountNumber, @RequestParam(name = "withdrawAmount") Double withdrawAmount) {
        if (withdrawAmount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Withdrawal amount must be provided");
        }

        if (withdrawAmount <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Withdrawal amount must be greater than zero");
        }

        try {

            AccountDTO accountDTO = accountService.withdraw(accountNumber, withdrawAmount);

            return ResponseEntity.ok(accountDTO);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}