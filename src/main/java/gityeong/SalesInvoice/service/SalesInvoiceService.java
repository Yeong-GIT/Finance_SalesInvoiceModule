package gityeong.SalesInvoice.service;

import gityeong.SalesInvoice.entity.SalesInvoice;
import gityeong.SalesInvoice.repository.SalesInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
public class SalesInvoiceService {
    @Autowired
    private SalesInvoiceRepository repository;

    public List<SalesInvoice> getAllSalesInvoicesInSequence(){
        return repository.findAll();
    }

    public SalesInvoice findById(Long id){
        Optional<SalesInvoice> optionalInvoice = repository.findById(id);
        if(optionalInvoice.isPresent()){
            return optionalInvoice.get();
        }else{
            throw new RuntimeException("Sales invoice with id" + id + "not found");
        }
    }

    public SalesInvoice createSalesInvoice(SalesInvoice invoice) {
        // Validate customerName is not null
        if (invoice.getCustomerName() == null || invoice.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (invoice.getInvoiceDate() == null) {
            throw new IllegalArgumentException("Invoice date cannot be null");
        }
        return repository.save(invoice);
    }

    public SalesInvoice updateCashReceipt(Long id, SalesInvoice invoice){
        Optional<SalesInvoice> optionalInvoice = repository.findById(id);
        if(optionalInvoice.isPresent()){
            SalesInvoice existingInvoice = optionalInvoice.get();
            existingInvoice.setCustomerName(invoice.getCustomerName());
            existingInvoice.setAmount(invoice.getAmount());
            existingInvoice.setInvoiceDate(invoice.getInvoiceDate());
            return repository.save(existingInvoice);
        }else{
            throw new RuntimeException("Sales Invoice with id " + id + "not found");
        }
    }

    public boolean deleteSalesInvoice(Long id){
        Optional<SalesInvoice> optionalInvoice = repository.findById(id);
        if(optionalInvoice.isPresent()){
            repository.deleteById(id);
            return true;
        }else{
            throw new RuntimeException("Sales invoice with id " + id + "not found");
        }
    }
}
