package io.credable.loanmanagement.repository;

import io.credable.loanmanagement.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    
    /**
     * Find customer by phone number
     * @param phoneNumber customer's phone number
     * @return Optional of Customer
     */
    Optional<Customer> findByPhoneNumber(String phoneNumber);
    
    /**
     * Check if a customer is subscribed
     * @param customerNumber customer's unique identifier
     * @return true if customer is subscribed
     */
    boolean existsByCustomerNumberAndIsSubscribedTrue(String customerNumber);
} 