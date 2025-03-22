package io.credable.loanmanagement.client;

import io.credable.loanmanagement.client.wsdl.GetCustomerRequest;
import io.credable.loanmanagement.client.wsdl.GetCustomerResponse;
import io.credable.loanmanagement.exception.CbsIntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class KycClient {
    private static final Logger log = LoggerFactory.getLogger(KycClient.class);
    private final WebServiceTemplate webServiceTemplate;

    public KycClient(@Qualifier("kycWebServiceTemplate") WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    /**
     * Get customer information from CBS
     * @param customerNumber customer's unique identifier
     * @return customer information response
     * @throws CbsIntegrationException if there's an error calling the CBS
     */
    public GetCustomerResponse getCustomerInfo(String customerNumber) {
        try {
            log.info("Fetching customer info for customer number: {}", customerNumber);
            GetCustomerRequest request = new GetCustomerRequest();
            request.setCustomerNumber(customerNumber);
            return (GetCustomerResponse) webServiceTemplate.marshalSendAndReceive(request);
        } catch (Exception e) {
            log.error("Error fetching customer info for customer number: {}", customerNumber, e);
            throw new CbsIntegrationException("Error fetching customer info", e);
        }
    }
} 