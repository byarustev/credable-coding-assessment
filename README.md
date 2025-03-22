# Loan Management System

A Spring Boot application that integrates with a Bank's Core Banking System (CBS) and Scoring Engine to process loan applications.

## Features

- Customer subscription management
- Loan application processing
- Integration with CBS for KYC and transaction data
- Integration with Scoring Engine for credit assessment
- RESTful APIs for mobile application

## Technology Stack

- Java 11
- Spring Boot 2.7.0
- Spring Data JPA
- Spring Security
- Spring Web Services
- H2 Database
- Maven
- Swagger/OpenAPI for documentation

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone [repository-url]
   ```

2. Navigate to the project directory:
   ```bash
   cd loan-management
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI Specification: `http://localhost:8080/v3/api-docs`

## Database Console

H2 Console is available at `http://localhost:8080/h2-console` with the following default credentials:
- JDBC URL: `jdbc:h2:mem:loandb`
- Username: `sa`
- Password: `password`

## Test Data

Available test customer IDs:
- 234774784
- 318411216
- 340397370
- 366585630
- 397178638
