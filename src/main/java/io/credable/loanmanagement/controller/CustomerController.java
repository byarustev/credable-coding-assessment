package io.credable.loanmanagement.controller;

import io.credable.loanmanagement.dto.CustomerDTO;
import io.credable.loanmanagement.service.CustomerService;
import io.credable.loanmanagement.util.DTOMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Management", description = "APIs for managing customer subscriptions")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/subscribe")
    @Operation(summary = "Subscribe a customer",
            description = "Subscribe a new customer or update existing customer information")
    public ResponseEntity<CustomerDTO> subscribeCustomer(
            @RequestParam @NotBlank String customerNumber) {
        return ResponseEntity.ok(
            DTOMapper.toCustomerDTO(customerService.subscribeCustomer(customerNumber))
        );
    }

    @GetMapping("/{customerNumber}")
    @Operation(summary = "Get customer information",
            description = "Retrieve customer information including subscription status")
    public ResponseEntity<CustomerDTO> getCustomerInfo(
            @PathVariable @NotBlank String customerNumber) {
        return ResponseEntity.ok(
            DTOMapper.toCustomerDTO(customerService.getCustomerInfo(customerNumber))
        );
    }

    @GetMapping("/{customerNumber}/subscription")
    @Operation(summary = "Check subscription status",
            description = "Check if a customer is subscribed")
    public ResponseEntity<Boolean> isCustomerSubscribed(
            @PathVariable @NotBlank String customerNumber) {
        return ResponseEntity.ok(customerService.isCustomerSubscribed(customerNumber));
    }
} 