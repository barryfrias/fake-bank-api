package org.blfrias.fakebank.service;
import org.blfrias.fakebank.dto.AccountDTO;
import org.blfrias.fakebank.dto.TransactionDTO;
import org.blfrias.fakebank.mapper.AccountMapper;
import org.blfrias.fakebank.mapper.TransactionMapper;
import org.blfrias.fakebank.model.Account;
import org.blfrias.fakebank.model.Transaction;
import org.blfrias.fakebank.repository.AccountRepository;
import org.blfrias.fakebank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        AccountDTO accountDTO = new AccountDTO();
        Account account = new Account();
        when(accountMapper.toEntity(any())).thenReturn(account);
        when(accountMapper.toDto(any())).thenReturn(accountDTO);
        when(accountRepository.save(any())).thenReturn(account);

        AccountDTO createdAccount = accountService.create(accountDTO);

        assertEquals(accountDTO, createdAccount);
        verify(accountRepository, times(1)).save(any());
    }

    @Test
    void testGetByAccountNumber() {
        AccountDTO accountDTO = new AccountDTO();
        Account account = new Account();
        when(accountMapper.toDto(any())).thenReturn(accountDTO);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));

        Optional<AccountDTO> retrievedAccount = accountService.getByAccountNumber("12345");

        assertEquals(accountDTO, retrievedAccount.orElse(null));
        verify(accountRepository, times(1)).findByAccountNumber(any());
    }

    @Test
    void testGetAllByCustomerId() {
        AccountDTO accountDTO1 = new AccountDTO();
        AccountDTO accountDTO2 = new AccountDTO();
        Account account1 = new Account();
        Account account2 = new Account();
        when(accountMapper.toDto(any())).thenReturn(accountDTO1, accountDTO2);
        when(accountRepository.findAllByCustomerId(any())).thenReturn(Arrays.asList(account1, account2));

        List<AccountDTO> retrievedAccounts = accountService.getAllByCustomerId("123");

        assertEquals(2, retrievedAccounts.size());
        verify(accountRepository, times(1)).findAllByCustomerId(any());
    }

    @Test
    void testTransferBalance() {
        Account sourceAccount = new Account();
        sourceAccount.setBalance(100.0);
        Account targetAccount = new Account();
        TransactionDTO transactionDTO = new TransactionDTO();
        when(accountRepository.findByAccountNumber("source")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("target")).thenReturn(Optional.of(targetAccount));
        when(transactionMapper.toDto(any())).thenReturn(transactionDTO);

        TransactionDTO result = accountService.transferBalance("source", "target", 50);

        assertEquals(transactionDTO, result);
        assertEquals(50, sourceAccount.getBalance());
        assertEquals(50, targetAccount.getBalance());
        verify(accountRepository, times(2)).save(any());
        verify(transactionRepository, times(2)).save(any());
    }

    @Test
    void testTransferBalanceInsufficientBalance() {
        Account sourceAccount = new Account();
        sourceAccount.setBalance(100.0);
        Account targetAccount = new Account();
        when(accountRepository.findByAccountNumber("source")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("target")).thenReturn(Optional.of(targetAccount));

        assertThrows(
            IllegalStateException.class,
            () -> accountService.transferBalance("source", "target", 150)
        );
        assertEquals(100, sourceAccount.getBalance());
        assertEquals(0, targetAccount.getBalance());
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testTransferBalanceAccountNotFound() {
        when(accountRepository.findByAccountNumber("source")).thenReturn(Optional.empty());

        assertThrows(
            IllegalStateException.class,
            () -> accountService.transferBalance("source", "target", 50)
        );
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testGetTransactionHistory() {
        TransactionDTO transactionDTO1 = new TransactionDTO();
        TransactionDTO transactionDTO2 = new TransactionDTO();
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        when(transactionMapper.toDto(any())).thenReturn(transactionDTO1, transactionDTO2);
        when(transactionRepository.findAllByAccountNumber(any())).thenReturn(Arrays.asList(transaction1, transaction2));

        List<TransactionDTO> retrievedTransactions = accountService.getTransactionHistory("123");

        assertEquals(2, retrievedTransactions.size());
        verify(transactionRepository, times(1)).findAllByAccountNumber(any());
    }

    @Test
    void testDeleteByAccountNumber() {
        AccountDTO accountDTO = new AccountDTO();
        Account account = new Account();
        when(accountMapper.toDto(any())).thenReturn(accountDTO);
        when(accountRepository.findByAccountNumber(any())).thenReturn(Optional.of(account));
        accountService.deleteByAccountNumber("123");
        verify(accountRepository, times(1)).deleteByAccountNumber(any());
    }



}