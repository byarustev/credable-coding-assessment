package io.credable.loanmanagement.exception;

public class CbsIntegrationException extends RuntimeException {
    
    public CbsIntegrationException(String message) {
        super(message);
    }
    
    public CbsIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }
} 