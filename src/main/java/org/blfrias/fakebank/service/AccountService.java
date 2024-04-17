package org.blfrias.fakebank.service;

import org.blfrias.fakebank.dto.AccountDTO;
import org.blfrias.fakebank.dto.TransactionDTO;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountDTO create(AccountDTO account);

    Optional<AccountDTO> getByAccountNumber(String accountId);

    List<AccountDTO> getAllByCustomerId(String customerId);

    boolean isValidAccount(String accountId);

    TransactionDTO transferBalance(String sourceAccountId, String targetAccountId, double amount);

    List<TransactionDTO> getTransactionHistory(String accountNumber);

    boolean deleteByAccountNumber(String accountNumber);

    AccountDTO deposit(String accountNumber, double amount);

    AccountDTO withdraw(String accountNumber, double amount);
}