package io.credable.loanmanagement.util;

import io.credable.loanmanagement.dto.CustomerDTO;
import io.credable.loanmanagement.dto.LoanApplicationDTO;
import io.credable.loanmanagement.model.Customer;
import io.credable.loanmanagement.model.LoanApplication;

public class DTOMapper {
    
    public static CustomerDTO toCustomerDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerNumber(customer.getCustomerNumber());
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setEmail(customer.getEmail());
        dto.setSubscribed(customer.isSubscribed());
        return dto;
    }
    
    public static LoanApplicationDTO toLoanApplicationDTO(LoanApplication application) {
        if (application == null) {
            return null;
        }
        
        LoanApplicationDTO dto = new LoanApplicationDTO();
        dto.setId(application.getId());
        dto.setCustomerNumber(application.getCustomer().getCustomerNumber());
        dto.setRequestedAmount(application.getRequestedAmount());
        dto.setStatus(application.getStatus());
        dto.setCreditScore(application.getCreditScore());
        dto.setApprovedAmount(application.getApprovedAmount());
        dto.setRejectionReason(application.getRejectionReason());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());
        return dto;
    }
} 