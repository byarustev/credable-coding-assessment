package io.credable.loanmanagement.service;

import io.credable.loanmanagement.exception.LoanApplicationException;
import io.credable.loanmanagement.model.Customer;
import io.credable.loanmanagement.model.LoanApplication;
import io.credable.loanmanagement.model.enums.LoanStatus;
import io.credable.loanmanagement.repository.LoanApplicationRepository;
import io.credable.loanmanagement.service.ScoringService.ScoreResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private static final Logger log = LoggerFactory.getLogger(LoanService.class);

    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomerService customerService;
    private final ScoringService scoringService;

    public LoanService(
            LoanApplicationRepository loanApplicationRepository,
            CustomerService customerService,
            ScoringService scoringService) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.customerService = customerService;
        this.scoringService = scoringService;
    }

    /**
     * Process a new loan application
     * @param customerNumber customer's unique identifier
     * @param amount requested loan amount
     * @return created loan application
     */
    @Transactional
    public LoanApplication applyForLoan(String customerNumber, BigDecimal amount) {
        log.info("Processing loan application for customer: {} amount: {}", customerNumber, amount);
        
        // Verify customer subscription
        if (!customerService.isCustomerSubscribed(customerNumber)) {
            throw new LoanApplicationException("Customer is not subscribed");
        }
        
        // Check for existing active loan applications
        Optional<LoanApplication> existingLoan = loanApplicationRepository
                .findFirstByCustomer_CustomerNumberAndStatusNotInOrderByCreatedAtDesc(
                    customerNumber,
                    LoanStatus.APPROVED,
                    LoanStatus.REJECTED,
                    LoanStatus.FAILED
                );
        
        if (existingLoan.isPresent()) {
            throw new LoanApplicationException("Customer has an active loan application");
        }
        
        // Create new loan application
        Customer customer = customerService.getCustomerInfo(customerNumber);
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setCustomer(customer);
        loanApplication.setRequestedAmount(amount);
        loanApplication.setStatus(LoanStatus.PENDING);
        
        // Save and initiate scoring
        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);
        String scoringToken = scoringService.initiateScoring(savedApplication);
        
        log.info("Loan application created with ID: {} and scoring token: {}", 
                savedApplication.getId(), scoringToken);
        
        return savedApplication;
    }

    /**
     * Process scoring result for a loan application
     * @param token scoring token
     * @return updated loan application
     */
    @Transactional
    public LoanApplication processScoringResult(String token) {
        log.info("Processing scoring result for token: {}", token);
        
        LoanApplication loanApplication = loanApplicationRepository
                .findByStatusAndScoringToken(LoanStatus.SCORING_IN_PROGRESS, token)
                .orElseThrow(() -> new LoanApplicationException("No active loan application found for token: " + token));
        
        try {
            ScoreResult scoreResult = scoringService.queryScore(token);
            
            // Update loan application with score result
            loanApplication.setCreditScore(scoreResult.getScore());
            
            // Process loan decision based on score and limit
            if (scoreResult.getScore() >= 700 && scoreResult.getLimit() != null) {
                BigDecimal approvedAmount = BigDecimal.valueOf(scoreResult.getLimit());
                if (approvedAmount.compareTo(loanApplication.getRequestedAmount()) >= 0) {
                    loanApplication.setStatus(LoanStatus.APPROVED);
                    loanApplication.setApprovedAmount(loanApplication.getRequestedAmount());
                } else {
                    loanApplication.setStatus(LoanStatus.APPROVED);
                    loanApplication.setApprovedAmount(approvedAmount);
                }
            } else {
                loanApplication.setStatus(LoanStatus.REJECTED);
                loanApplication.setRejectionReason("Credit score too low or limit not available");
            }
            
        } catch (Exception e) {
            log.error("Error processing scoring result", e);
            loanApplication.setStatus(LoanStatus.FAILED);
            loanApplication.setRejectionReason("Error processing scoring result");
        }
        
        return loanApplicationRepository.save(loanApplication);
    }

    /**
     * Get loan application status
     * @param customerNumber customer's unique identifier
     * @return list of loan applications
     */
    @Transactional(readOnly = true)
    public List<LoanApplication> getLoanApplications(String customerNumber) {
        return loanApplicationRepository
                .findByCustomer_CustomerNumberOrderByCreatedAtDesc(customerNumber);
    }
} 