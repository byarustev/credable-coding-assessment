package io.credable.loanmanagement.exception;

public class ScoringException extends RuntimeException {
    
    public ScoringException(String message) {
        super(message);
    }
    
    public ScoringException(String message, Throwable cause) {
        super(message, cause);
    }
} 