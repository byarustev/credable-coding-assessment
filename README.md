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

## Environment Variables Setup

The application uses environment variables for configuration. Create a `.env` file in the root directory using the `.env.template` as a reference:

### Getting the Scoring Client Token

Before setting up the environment variables, you need to obtain a SCORING_CLIENT_TOKEN by registering your application with the scoring service:

1. Register your client using this curl command:
   ```bash
   curl -X POST "https://scoringtest.credable.io/api/v1/client/createClient" \
     -H "Content-Type: application/json" \
     -H "Authorization: Basic $(echo -n admin:pwd123 | base64)" \
     -d '{
       "url": "http://your-host:8080/api/v1/transactions",
       "name": "loan-management",
       "username": "OWN_USERNAME",
       "password": "OWN_PASSWORD"
     }'
   ```
   Replace `your-host` with your application's host URL.
   Note: "OWN_USERNAME" and "OWN_PASSWORD" should be replaced with your own chosen credentials for this application.

2. The response will contain your client token:
   ```json
   {
     "token": "your-client-token"
   }
   ```

3. Use this token as your `SCORING_CLIENT_TOKEN` in the environment variables.

### Environment Variables

```bash
# Core Banking System (CBS) Integration
CBS_KYC_URL=https://kycapitest.credable.io/service/customerWsdl.wsdl
CBS_TRX_URL=https://trxapitest.credable.io/service/transactionWsdl.wsdl
CBS_USERNAME=****    # Replace with your CBS username
CBS_PASSWORD=****    # Replace with your CBS password

# API Security
API_USERNAME=OWN_USERNAME    # Replace with your chosen API username
API_PASSWORD=OWN_PASSWORD    # Replace with your chosen API password

# Scoring Engine Configuration
SCORING_BASE_URL=https://scoringtest.credable.io/api/v1
SCORING_CLIENT_TOKEN=Application-client-token    # Replace with token from registration step
SCORING_MAX_RETRIES=3
SCORING_RETRY_DELAY=1000

# Database Configuration
SPRING_DATASOURCE_URL=jdbc:h2:mem:loandb
SPRING_DATASOURCE_USERNAME=sa    # Replace if using different database
SPRING_DATASOURCE_PASSWORD=password    # Replace if using different database

# Application Configuration
SPRING_PROFILES_ACTIVE=local    # Use 'prod' for production
SERVER_PORT=8080

# H2 Console (Development only)
SPRING_H2_CONSOLE_ENABLED=true    # Set to false in production
SPRING_H2_CONSOLE_PATH=/h2-console

# JPA Configuration
SPRING_JPA_SHOW_SQL=true    # Set to false in production
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

**Important Notes:**
1. For production deployment, set appropriate values for database and security configurations
2. Make sure your Transaction API endpoint is accessible to the scoring service

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone [repository-url]
   ```

2. Navigate to the project directory:
   ```bash
   cd loan-management
   ```

3. Create your `.env` file:
   ```bash
   cp .env.template .env
   # Edit .env with your configuration values
   ```

4. Build the project:
   ```bash
   mvn clean install
   ```

5. Run the application:
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
