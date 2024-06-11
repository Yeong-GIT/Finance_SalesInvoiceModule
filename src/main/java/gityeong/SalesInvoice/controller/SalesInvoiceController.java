package gityeong.SalesInvoice.controller;

import gityeong.SalesInvoice.entity.SalesInvoice;
import gityeong.SalesInvoice.service.SalesInvoiceService;
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
        return new ResponseEntity<>(savedInvoice, HttpStatus.CREATED);
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<SalesInvoice> updateSalesInvoice(@PathVariable Long id, @RequestBody SalesInvoice invoice){
        SalesInvoice updateInvoice = service.updateCashReceipt(id, invoice);
        if(updateInvoice != null){
            return new ResponseEntity<>(updateInvoice, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteSalesInvoice(@PathVariable Long id){
        try{
            service.deleteSalesInvoice(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



}
