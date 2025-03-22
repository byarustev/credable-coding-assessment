package io.credable.loanmanagement.client;

import io.credable.loanmanagement.client.wsdl.GetTransactionsRequest;
import io.credable.loanmanagement.client.wsdl.GetTransactionsResponse;
import io.credable.loanmanagement.exception.CbsIntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

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
     * @param startDate start of date range
     * @param endDate end of date range
     * @return transaction data response
     * @throws CbsIntegrationException if there's an error calling the CBS
     */
    public GetTransactionsResponse getCustomerTransactions(String customerNumber, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            log.info("Fetching transactions for customer: {} between {} and {}", customerNumber, startDate, endDate);
            GetTransactionsRequest request = new GetTransactionsRequest();
            request.setCustomerNumber(customerNumber);
            
            if (startDate != null) {
                request.setStartDate(toXMLGregorianCalendar(startDate));
            }
            if (endDate != null) {
                request.setEndDate(toXMLGregorianCalendar(endDate));
            }
            
            return (GetTransactionsResponse) webServiceTemplate.marshalSendAndReceive(request);
        } catch (Exception e) {
            log.error("Error fetching transactions for customer: {}", customerNumber, e);
            throw new CbsIntegrationException("Error fetching transactions", e);
        }
    }

    private XMLGregorianCalendar toXMLGregorianCalendar(LocalDateTime dateTime) {
        try {
            GregorianCalendar cal = GregorianCalendar.from(dateTime.atZone(ZoneId.systemDefault()));
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {
            throw new CbsIntegrationException("Error converting date", e);
        }
    }
} 