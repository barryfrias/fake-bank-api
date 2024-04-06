package org.blfrias.fakebank.repository;

import org.blfrias.fakebank.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findAllByAccountNumber(String accountNumber);
}