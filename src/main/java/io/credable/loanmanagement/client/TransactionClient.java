package io.credable.loanmanagement.client;

import io.credable.loanmanagement.exception.CbsIntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionClient {

    private final WebServiceTemplate transactionWebServiceTemplate;

    /**
     * Get customer transactions from CBS
     * @param customerNumber customer's unique identifier
     * @param startDate start of date range
     * @param endDate end of date range
     * @return transaction data response
     * @throws CbsIntegrationException if there's an error calling the CBS
     */
    public Object getCustomerTransactions(String customerNumber, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // TODO: Replace Object with generated WSDL class
            // GetTransactionsRequest request = new GetTransactionsRequest();
            // request.setCustomerNumber(customerNumber);
            // request.setStartDate(startDate);
            // request.setEndDate(endDate);
            // return (GetTransactionsResponse) transactionWebServiceTemplate.marshalSendAndReceive(request);
            
            log.info("Fetching transactions for customer: {} between {} and {}", 
                    customerNumber, startDate, endDate);
            return null;
        } catch (Exception e) {
            log.error("Error fetching transactions for customer: {}", customerNumber, e);
            throw new CbsIntegrationException("Failed to fetch customer transactions from CBS", e);
        }
    }
} 