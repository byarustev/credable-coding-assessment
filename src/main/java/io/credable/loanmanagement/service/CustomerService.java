package io.credable.loanmanagement.service;

import io.credable.loanmanagement.client.KycClient;
import io.credable.loanmanagement.client.wsdl.CustomerResponse;
import io.credable.loanmanagement.exception.CbsIntegrationException;
import io.credable.loanmanagement.model.Customer;
import io.credable.loanmanagement.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final KycClient kycClient;

    public CustomerService(CustomerRepository customerRepository, KycClient kycClient) {
        this.customerRepository = customerRepository;
        this.kycClient = kycClient;
    }

    /**
     * Subscribe a new customer or update existing customer information
     * @param customerNumber CBS customer number
     * @return Updated customer information
     */
    @Transactional
    public Customer subscribeCustomer(String customerNumber) {
        log.info("Processing subscription for customer: {}", customerNumber);
        
        // Get customer information from CBS
        CustomerResponse kycResponse = kycClient.getCustomerInfo(customerNumber);
        
        // Create or update customer record
        Customer customer = customerRepository.findById(customerNumber)
                .orElse(new Customer());
        
        // Update customer information
        customer.setCustomerNumber(customerNumber);
        customer.setFirstName(kycResponse.getCustomer().getFirstName());
        customer.setLastName(kycResponse.getCustomer().getLastName());
        customer.setPhoneNumber(kycResponse.getCustomer().getMobile());
        customer.setEmail(kycResponse.getCustomer().getEmail());
        customer.setSubscribed(true);
        
        // Save customer
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer subscription processed successfully: {}", customerNumber);
        
        return savedCustomer;
    }

    /**
     * Verify if a customer exists and is subscribed
     * @param customerNumber CBS customer number
     * @return true if customer exists and is subscribed
     */
    public boolean isCustomerSubscribed(String customerNumber) {
        return customerRepository.existsByCustomerNumberAndSubscribedTrue(customerNumber);
    }

    /**
     * Get customer information, fetching from CBS if not found locally
     * @param customerNumber CBS customer number
     * @return Customer information
     */
    @Transactional(readOnly = true)
    public Customer getCustomerInfo(String customerNumber) {
        log.info("Fetching customer information for: {}", customerNumber);
        
        return customerRepository.findById(customerNumber)
                .orElseThrow(() -> {
                    log.error("Customer not found: {}", customerNumber);
                    return new CbsIntegrationException("Customer not found: " + customerNumber);
                });
    }
} 