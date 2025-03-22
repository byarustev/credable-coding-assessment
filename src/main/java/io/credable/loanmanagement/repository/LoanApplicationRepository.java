package io.credable.loanmanagement.repository;

import io.credable.loanmanagement.model.LoanApplication;
import io.credable.loanmanagement.model.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    
    /**
     * Find all loan applications for a customer
     * @param customerNumber customer's unique identifier
     * @return list of loan applications
     */
    List<LoanApplication> findByCustomer_CustomerNumberOrderByCreatedAtDesc(String customerNumber);
    
    /**
     * Find active loan applications (those not in final states) for a customer
     * @param customerNumber customer's unique identifier
     * @param status1 first status to exclude
     * @param status2 second status to exclude
     * @param status3 third status to exclude
     * @return Optional of LoanApplication
     */
    Optional<LoanApplication> findFirstByCustomer_CustomerNumberAndStatusNotInOrderByCreatedAtDesc(
            String customerNumber,
            LoanStatus status1,
            LoanStatus status2,
            LoanStatus status3
    );
    
    /**
     * Find loan applications by status and scoring token
     * @param status loan application status
     * @param scoringToken scoring engine token
     * @return Optional of LoanApplication
     */
    Optional<LoanApplication> findByStatusAndScoringToken(LoanStatus status, String scoringToken);
} 