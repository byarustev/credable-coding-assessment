package io.credable.loanmanagement.repository;

import io.credable.loanmanagement.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    /**
     * Find customer transactions within a date range
     * @param customerNumber customer's unique identifier
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of transactions
     */
    List<Transaction> findByCustomer_CustomerNumberAndTransactionDateBetweenOrderByTransactionDateDesc(
            String customerNumber,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
    
    /**
     * Find transactions by account number
     * @param accountNumber customer's account number
     * @return list of transactions
     */
    List<Transaction> findByAccountNumberOrderByTransactionDateDesc(String accountNumber);
    
    /**
     * Find transaction by its reference number
     * @param transactionReference unique transaction reference
     * @return list of transactions (should typically be one)
     */
    List<Transaction> findByTransactionReference(String transactionReference);
} 