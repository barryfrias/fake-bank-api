package org.blfrias.fakebank.service;

import lombok.RequiredArgsConstructor;
import org.blfrias.fakebank.dto.AccountDTO;
import org.blfrias.fakebank.dto.TransactionDTO;
import org.blfrias.fakebank.mapper.AccountMapper;
import org.blfrias.fakebank.mapper.TransactionMapper;
import org.blfrias.fakebank.model.Transaction;
import org.blfrias.fakebank.repository.AccountRepository;
import org.blfrias.fakebank.repository.TransactionRepository;
import org.blfrias.fakebank.util.BankAccountNumberGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public AccountDTO create(AccountDTO accountDTO) {
        accountDTO.setAccountNumber(BankAccountNumberGenerator.generateAccountNumber());
        return accountMapper.toDto(accountRepository.save(accountMapper.toEntity(accountDTO)));
    }

    @Override
    public Optional<AccountDTO> getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).map(accountMapper::toDto);
    }

    @Override
    public List<AccountDTO> getAllByCustomerId(String customerId) {
        return accountRepository.findAllByCustomerId(customerId).stream()
            .map(accountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public boolean isValidAccount(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    @Override
    @Transactional
    public TransactionDTO transferBalance(String sourceAccountNumber, String targetAccountNumber, double amount) {
        var sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
            .orElseThrow(() -> new IllegalStateException("Source account not found"));

        var targetAccount = accountRepository.findByAccountNumber(targetAccountNumber)
            .orElseThrow(() -> new IllegalStateException("Target account not found"));

        if (sourceAccount.getBalance() < amount) {
            throw new IllegalStateException("Insufficient balance in the source account");
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        targetAccount.setBalance(targetAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        var txn = Transaction.builder()
            .accountNumber(sourceAccountNumber)
            .amount(amount)
            .description("Transferred " + amount + " to " + targetAccountNumber)
            .timestamp(LocalDateTime.now()).build();
        var txnDTO = transactionMapper.toDto(transactionRepository.save(txn));

        accountRepository.save(targetAccount);
        txn = Transaction.builder()
            .accountNumber(targetAccountNumber)
            .amount(amount)
            .description("Received " + amount + " from " + sourceAccountNumber)
            .timestamp(LocalDateTime.now()).build();
        transactionRepository.save(txn);

        return txnDTO;
    }

    @Override
    public List<TransactionDTO> getTransactionHistory(String accountNumber) {
        return transactionRepository.findAllByAccountNumber(accountNumber).stream()
            .map(transactionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}