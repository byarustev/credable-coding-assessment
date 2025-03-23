package io.credable.loanmanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private String accountNumber;
    private LocalDateTime transactionDate;
    private BigDecimal amount;
    private String type;
    private BigDecimal balance;

    private TransactionDTO(Builder builder) {
        this.accountNumber = builder.accountNumber;
        this.transactionDate = builder.transactionDate;
        this.amount = builder.amount;
        this.type = builder.type;
        this.balance = builder.balance;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    // Builder class
    public static class Builder {
        private String accountNumber;
        private LocalDateTime transactionDate;
        private BigDecimal amount;
        private String type;
        private BigDecimal balance;

        public Builder accountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder transactionDate(LocalDateTime transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public Builder amount(Double amount) {
            this.amount = amount != null ? BigDecimal.valueOf(amount) : null;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder balance(Double balance) {
            this.balance = balance != null ? BigDecimal.valueOf(balance) : null;
            return this;
        }

        public TransactionDTO build() {
            return new TransactionDTO(this);
        }
    }
} 