package io.credable.loanmanagement.controller;

import io.credable.loanmanagement.dto.LoanApplicationDTO;
import io.credable.loanmanagement.service.LoanService;
import io.credable.loanmanagement.util.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/loans")
@Tag(name = "Loan Management", description = "APIs for managing loan applications")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/apply")
    @Operation(summary = "Apply for a loan",
            description = "Submit a new loan application for processing")
    public ResponseEntity<LoanApplicationDTO> applyForLoan(
            @RequestBody @Valid LoanApplicationDTO request) {
        return ResponseEntity.ok(
            DTOMapper.toLoanApplicationDTO(
                loanService.applyForLoan(
                    request.getCustomerNumber(),
                    request.getRequestedAmount()
                )
            )
        );
    }

    @GetMapping("/customer/{customerNumber}")
    @Operation(summary = "Get customer's loan applications",
            description = "Retrieve all loan applications for a customer")
    public ResponseEntity<List<LoanApplicationDTO>> getLoanApplications(
            @PathVariable String customerNumber) {
        return ResponseEntity.ok(
            loanService.getLoanApplications(customerNumber)
                .stream()
                .map(DTOMapper::toLoanApplicationDTO)
                .collect(Collectors.toList())
        );
    }

    @PostMapping("/scoring/{token}")
    @Operation(summary = "Process scoring result",
            description = "Process the scoring result for a loan application")
    public ResponseEntity<LoanApplicationDTO> processScoringResult(
            @PathVariable String token) {
        return ResponseEntity.ok(
            DTOMapper.toLoanApplicationDTO(
                loanService.processScoringResult(token)
            )
        );
    }
} 