package io.credable.loanmanagement.controller;

import io.credable.loanmanagement.client.TransactionClient;
import io.credable.loanmanagement.client.wsdl.TransactionsResponse;
import io.credable.loanmanagement.client.wsdl.TransactionData;
import io.credable.loanmanagement.dto.TransactionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transactions")
@Tag(name = "Transaction Data", description = "APIs for accessing customer transaction data")
public class TransactionDataController {
    
    private static final Logger log = LoggerFactory.getLogger(TransactionDataController.class);
    private final TransactionClient transactionClient;

    public TransactionDataController(TransactionClient transactionClient) {
        this.transactionClient = transactionClient;
    }

    @GetMapping("/{customerNumber}")
    @Operation(summary = "Get customer transactions",
            description = "Retrieve customer transaction data for scoring",
            security = @SecurityRequirement(name = "basic"))
    public ResponseEntity<List<TransactionDTO>> getTransactions(
            @PathVariable String customerNumber,
            @RequestHeader("client-token") String clientToken) {
        
        log.info("Received transaction data request for customer: {}", customerNumber);
        
        // Get transactions from CBS
        TransactionsResponse response = transactionClient.getCustomerTransactions(customerNumber);
        
        // Transform CBS response to scoring engine format
        List<TransactionDTO> transactions = response.getTransactions().stream()
                .map(t -> {
                    LocalDateTime txDate = t.getLastTransactionDate() != null 
                            ? LocalDateTime.ofInstant(
                                    t.getLastTransactionDate().toGregorianCalendar().toInstant(),
                                    ZoneId.systemDefault())
                            : null;
                            
                    return TransactionDTO.builder()
                            .accountNumber(t.getAccountNumber())
                            .transactionDate(txDate)
                            .amount(t.getTransactionValue())
                            .type(t.getLastTransactionType())
                            .balance(t.getMonthlyBalance())
                            .build();
                })
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(transactions);
    }
} 