package io.credable.loanmanagement.client;

import io.credable.loanmanagement.exception.CbsIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KycClient {

    private final WebServiceTemplate kycWebServiceTemplate;

    /**
     * Get customer information from CBS
     * @param customerNumber customer's unique identifier
     * @return customer information response
     * @throws CbsIntegrationException if there's an error calling the CBS
     */
    public Object getCustomerInfo(String customerNumber) {
        try {
            // TODO: Replace Object with generated WSDL class
            // GetCustomerInfoRequest request = new GetCustomerInfoRequest();
            // request.setCustomerNumber(customerNumber);
            // return (GetCustomerInfoResponse) kycWebServiceTemplate.marshalSendAndReceive(request);
            
            log.info("Fetching customer information for customer: {}", customerNumber);
            return null;
        } catch (Exception e) {
            log.error("Error fetching customer information for customer: {}", customerNumber, e);
            throw new CbsIntegrationException("Failed to fetch customer information from CBS", e);
        }
    }
} 