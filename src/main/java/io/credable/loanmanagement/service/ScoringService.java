package io.credable.loanmanagement.service;

import io.credable.loanmanagement.exception.ScoringException;
import io.credable.loanmanagement.model.LoanApplication;
import io.credable.loanmanagement.model.enums.LoanStatus;
import io.credable.loanmanagement.repository.LoanApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScoringService {
    private static final Logger log = LoggerFactory.getLogger(ScoringService.class);

    @Value("${scoring.base-url}")
    private String scoringBaseUrl;

    @Value("${scoring.max-retries}")
    private int maxRetries;

    @Value("${scoring.retry-delay}")
    private long retryDelay;

    private final RestTemplate restTemplate;
    private final LoanApplicationRepository loanApplicationRepository;

    public ScoringService(RestTemplate restTemplate, LoanApplicationRepository loanApplicationRepository) {
        this.restTemplate = restTemplate;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    /**
     * Initiate the scoring process for a loan application
     * @param loanApplication the loan application to score
     * @return scoring token
     */
    public String initiateScoring(LoanApplication loanApplication) {
        log.info("Initiating scoring for loan application: {}", loanApplication.getId());
        
        String url = scoringBaseUrl + "/scoring/initiateQueryScore/" + loanApplication.getCustomer().getCustomerNumber();
        
        try {
            ResponseEntity<InitiateScoreResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHttpEntity(),
                InitiateScoreResponse.class
            );
            
            if (response.getBody() != null && response.getBody().getToken() != null) {
                String token = response.getBody().getToken();
                loanApplication.setScoringToken(token);
                loanApplication.setStatus(LoanStatus.SCORING_IN_PROGRESS);
                loanApplicationRepository.save(loanApplication);
                return token;
            }
            
            throw new ScoringException("Failed to get scoring token");
        } catch (Exception e) {
            log.error("Error initiating scoring for loan application: {}", loanApplication.getId(), e);
            throw new ScoringException("Error initiating scoring process", e);
        }
    }

    /**
     * Query the score result with retry mechanism
     * @param token scoring token
     * @return score result
     */
    public ScoreResult queryScore(String token) {
        log.info("Querying score for token: {}", token);
        
        int retryCount = 0;
        Exception lastException = null;
        
        while (retryCount < maxRetries) {
            try {
                String url = scoringBaseUrl + "/scoring/queryScore/" + token;
                ResponseEntity<ScoreResult> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createHttpEntity(),
                    ScoreResult.class
                );
                
                if (response.getBody() != null) {
                    return response.getBody();
                }
                
                throw new ScoringException("Empty response from scoring service");
            } catch (Exception e) {
                lastException = e;
                retryCount++;
                
                if (retryCount < maxRetries) {
                    log.warn("Retry {} of {} for token: {}", retryCount, maxRetries, token);
                    sleep(retryDelay * retryCount); // Exponential backoff
                }
            }
        }
        
        log.error("Failed to query score after {} retries", maxRetries);
        throw new ScoringException("Failed to query score after " + maxRetries + " retries", lastException);
    }

    private HttpEntity<Void> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("client-token", "your-client-token"); // TODO: Configure client token
        return new HttpEntity<>(headers);
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ScoringException("Interrupted while waiting for retry", e);
        }
    }

    // Inner classes for response mapping
    private static class InitiateScoreResponse {
        private String token;
        
        public String getToken() {
            return token;
        }
        
        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class ScoreResult {
        private Integer score;
        private Double limit;
        
        public Integer getScore() {
            return score;
        }
        
        public void setScore(Integer score) {
            this.score = score;
        }
        
        public Double getLimit() {
            return limit;
        }
        
        public void setLimit(Double limit) {
            this.limit = limit;
        }
    }
} 