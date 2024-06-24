package gityeong.SalesInvoice.controller;

import gityeong.SalesInvoice.entity.SalesInvoice;
import gityeong.SalesInvoice.service.SalesInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import gityeong.SalesInvoice.service.KafkaProducerService;
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
