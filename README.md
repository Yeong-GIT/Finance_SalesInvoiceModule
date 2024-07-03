# Finance Microservice CRUD Sales Invoice System

## Overview

This project is a fullstack dockerized microservice system focused on CRUD (Create, Read, Update, Delete) functionalities for financial sales invoice activities. It uses Spring Boot with Kafka integration and incorporates Python scripts for insert invoice automation.

## Features

- CRUD operations for sales invoice financial data
- Dockerized microservice architecture
- Spring Boot backend
- Python script integration for automated invoice generation
- Kafka integration for real-time data processing
- React frontend integrated with DataTable plugins

## Architecture

![ArchitectureDiagram1](https://github.com/Yeong-GIT/Finance_SalesInvoiceModule/assets/49313115/1c5ee928-5c66-4c6e-aef8-5884d5885b54)


## Prerequisites
- Node.js 18 or above
- Docker and Docker Compose
- Java JDK 17 or later
- Python 3.7 or later
- Maven
- PostgreSQL

## Setup and Installation

1. Clone the repository:
   ```
   git clone https://github.com/Yeong-GIT/Finance_SalesInvoiceModule.git
   cd Finance_SalesInvoice
   ```

   ## Backend Setup
2. Ensure Docker desktop application has opened:
   ```
   Start-Process "C:\Program Files\Docker\Docker\Docker Desktop.exe"
   ```

3. Build the Spring Boot application:
   ```
   mvn clean install
   ```

## Frontend Setup
To run the frontend in development mode:

1. Navigate to the frontend directory:
   ```
   cd src/frontend
   ```

2. Install dependencies:
   ```
   npm install
   ```

## Docker Setup
1. Build and run the Docker containers:
   ```
   docker-compose up --build
   ```
![salesinvoice](https://github.com/Yeong-GIT/Finance_SalesInvoiceModule/assets/49313115/594c729a-3070-404c-843d-9ebd83998de7)

## Frontend Development
### Data Table Plugins
- Pagination, Sort and Search Functionality
- CRUD actions with Generate, Create, Update, Delete Buttons

![Salesinvoicefrontend](https://github.com/Yeong-GIT/Finance_SalesInvoiceModule/assets/49313115/ba1bd506-ecec-4a6f-8517-16d7d13ec92d)

## Backend Development
### API Endpoints
- `GET /api/invoices`: Retrieve all sales invoices records
- `GET /api/invoices/{id}`: Retrieve a specific sales invoice record
- `POST /api/invoices`: Create a new sales invoice record
- `PUT /api/invoices/{id}`: Update an existing sales invoice record
- `DELETE /api/invoices/{id}`: Delete a sales invoice record

## Code Examples

### SalesInvoiceController

```java
package gityeong.SalesInvoice.controller;

import gityeong.SalesInvoice.entity.SalesInvoice;
import gityeong.SalesInvoice.service.SalesInvoiceService;
import gityeong.SalesInvoice.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SalesInvoiceController {
    @Autowired
    private SalesInvoiceService service;

    @Autowired
    private KafkaProducerService kafkaProducerService; // Inject KafkaProducerService

    @GetMapping("/invoices")
    public ResponseEntity<List<SalesInvoice>> getAllSalesInvoices(){
        return new ResponseEntity<>(service.getAllSalesInvoicesInSequence(), HttpStatus.OK);
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<SalesInvoice> getSalesInvoiceById(@PathVariable Long id){
        try {
            SalesInvoice invoice = service.findById(id);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/invoices")
    public ResponseEntity<SalesInvoice> createSalesInvoice(@RequestBody SalesInvoice invoice) {
        // Logging to check the invoice object before saving
        System.out.println("Received invoice: " + invoice);

        SalesInvoice savedInvoice = service.createSalesInvoice(invoice);
        // Send Kafka message after successfully creating the sales invoice
        String message = String.format("Invoice created: ID=%d, Customer=%s, Amount=%.2f, Date=%s",
                savedInvoice.getId(), savedInvoice.getCustomerName(), savedInvoice.getAmount(), savedInvoice.getInvoiceDate());
        kafkaProducerService.sendMessage("sales-invoice-topic", message);

        return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<SalesInvoice> updateSalesInvoice(@PathVariable Long id, @RequestBody SalesInvoice invoice){
        SalesInvoice updateInvoice = service.updateCashReceipt(id, invoice);
        if(updateInvoice != null){
            // Send Kafka message after successfully updating the sales invoice
            String message = String.format("Invoice updated: ID=%d, Customer=%s, Amount=%.2f, Date=%s",
                    updateInvoice.getId(), updateInvoice.getCustomerName(), updateInvoice.getAmount(), updateInvoice.getInvoiceDate());
            kafkaProducerService.sendMessage("sales-invoice-topic", message);
            return new ResponseEntity<>(updateInvoice, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteSalesInvoice(@PathVariable Long id){
        try{
            service.deleteSalesInvoice(id);

            // Send Kafka message after successfully deleting the sales invoice
            String message = String.format("Invoice deleted: ID=%d", id);
            kafkaProducerService.sendMessage("sales-invoice-topic", message);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
```

This controller handles CRUD operations for sales invoice and integrates with Kafka for messaging.

### Kafka Topics
- `sales-invoice-topic`: Produce message to prompt message for CRUD actions

![KafkaMessageSalesinvoice](https://github.com/Yeong-GIT/Finance_SalesInvoiceModule/assets/49313115/2ab33d1d-78a3-41ba-8c5d-d37fd6983c34)

### Python Scripts

- `generateSalesInvoices.py`: Creates 10 random receipts when "Generate Sales Invoice" button pressed

## Code Examples

### generateSalesInvoices.py

```python
from faker import Faker
import random
import requests

faker = Faker()

def generate_sales_invoice():
    invoice = {
        "customerName": faker.name(),
        "amount": round(random.uniform(10.0, 1000.0), 2),
        "invoiceDate": faker.date_this_year().isoformat()
    }
    print("Generated Invoice:", invoice)
    return invoice

def post_sales_invoice():
    invoice = generate_sales_invoice()
    # Use the service name and internal port since the script runs within Docker's network.
    response = requests.post('http://sales-invoice-service:8080/api/invoices', json=invoice)
    print("Response:", response.json())
    return response.json()

if __name__ == "__main__":
    for _ in range(10):
        print(post_sales_invoice())
```

This python scripts creates random 10 receipts per click with "Generate Sales Invoice" button.

![GenerateButton](https://github.com/Yeong-GIT/Finance_CashReceiptModule/assets/49313115/e0bb4017-bc0f-4200-b39b-b68dc7e23727)

## Configuration

- Spring Boot configuration: `src/main/resources/application.properties`
- Docker and Kafka configuration: `src/docker-compose.yml`

## Docker File Examples
```Backend Dockerfile
# Use OpenJDK 18
FROM openjdk:18-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY /target/SalesInvoice-0.0.1-SNAPSHOT.jar /app/SalesInvoice-0.0.1-SNAPSHOT.jar

# Copy the Python script into the container
COPY src/main/scripts/generateSalesInvoices.py /app/scripts/generateSalesInvoices.py

# Update package lists and install Python 3 and pip
RUN apt-get update && apt-get install -y python3 python3-pip

# Install the required Python packages
RUN pip3 install requests faker

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "SalesInvoice-0.0.1-SNAPSHOT.jar"]
```

This Dockerfile sets up a Docker container for running a Java application along with a Python script.

## Deployment

The system is containerized and can be deployed to any Docker-compatible environment.


## License

This project is open source.

## Acknowledgments

- Spring Boot
- Apache Kafka
- Docker
- Data Table Plugins
- Python community for excellent libraries

