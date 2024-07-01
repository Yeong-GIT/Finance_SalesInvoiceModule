package gityeong.SalesInvoice.controller;

import gityeong.SalesInvoice.entity.SalesInvoice;
import gityeong.SalesInvoice.service.SalesInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SalesInvoiceController {

    @Autowired
    private SalesInvoiceService service;

    @GetMapping("/invoices")
    public ResponseEntity<List<SalesInvoice>> getAllSalesInvoices() {
        List<SalesInvoice> invoices = service.getAllSalesInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<SalesInvoice> getSalesInvoiceById(@PathVariable Long id) {
        Optional<SalesInvoice> invoiceOpt = service.findById(id);
        return invoiceOpt.map(invoice -> new ResponseEntity<>(invoice, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/invoices")
    public ResponseEntity<SalesInvoice> createSalesInvoice(@RequestBody SalesInvoice invoice) {
        try {
            SalesInvoice savedInvoice = service.createSalesInvoice(invoice);
            return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<SalesInvoice> updateSalesInvoice(@PathVariable Long id, @RequestBody SalesInvoice invoice) {
        try {
            SalesInvoice updatedInvoice = service.updateSalesInvoice(id, invoice);
            return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteSalesInvoice(@PathVariable Long id) {
        try {
            service.deleteSalesInvoice(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
