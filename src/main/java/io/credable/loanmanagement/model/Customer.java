package io.credable.loanmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "customers")
public class Customer {
    
    @Id
    @NotNull
    private String customerNumber;  // Using the CBS customer number as the primary key
    
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    
    @Column(nullable = false)
    private boolean isSubscribed;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 