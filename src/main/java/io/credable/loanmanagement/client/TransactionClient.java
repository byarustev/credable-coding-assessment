package io.credable.loanmanagement.client;

import io.credable.loanmanagement.client.wsdl.TransactionsRequest;
import io.credable.loanmanagement.client.wsdl.TransactionsResponse;
import io.credable.loanmanagement.exception.CbsIntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import java.time.LocalDateTime;

@Component
public class TransactionClient {

    private static final Logger log = LoggerFactory.getLogger(TransactionClient.class);

    private final WebServiceTemplate webServiceTemplate;

    public TransactionClient(@Qualifier("transactionWebServiceTemplate") WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    /**
     * Get customer transactions from CBS
     * @param customerNumber customer's unique identifier
     * @return transaction data response
     * @throws CbsIntegrationException if there's an error calling the CBS
     */
    public TransactionsResponse getCustomerTransactions(String customerNumber) {
        try {
            log.info("Fetching transactions for customer: {}", customerNumber);
            TransactionsRequest request = new TransactionsRequest();
            request.setCustomerNumber(customerNumber);
            return (TransactionsResponse) webServiceTemplate.marshalSendAndReceive(request);
        } catch (Exception e) {
            log.error("Error fetching transactions for customer: {}", customerNumber, e);
            throw new CbsIntegrationException("Error fetching transactions", e);
        }
    }
} 