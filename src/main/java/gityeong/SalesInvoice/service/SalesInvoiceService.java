package gityeong.SalesInvoice.service;

import gityeong.SalesInvoice.entity.SalesInvoice;
import gityeong.SalesInvoice.repository.SalesInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SalesInvoiceService {

    @Autowired
    private SalesInvoiceRepository repository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public List<SalesInvoice> getAllSalesInvoices() {
        return repository.findAll();
    }

    public Optional<SalesInvoice> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public SalesInvoice createSalesInvoice(SalesInvoice invoice) {
        // Validate customerName is not null
        if (invoice.getCustomerName() == null || invoice.getCustomerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (invoice.getInvoiceDate() == null) {
            throw new IllegalArgumentException("Invoice date cannot be null");
        }

        SalesInvoice savedInvoice = repository.save(invoice);
        kafkaProducerService.sendMessage(savedInvoice);
        return savedInvoice;
    }

    @Transactional
    public SalesInvoice updateSalesInvoice(Long id, SalesInvoice invoice) {
        Optional<SalesInvoice> optionalInvoice = repository.findById(id);
        if (optionalInvoice.isPresent()) {
            SalesInvoice existingInvoice = optionalInvoice.get();
            existingInvoice.setCustomerName(invoice.getCustomerName());
            existingInvoice.setAmount(invoice.getAmount());
            existingInvoice.setInvoiceDate(invoice.getInvoiceDate());

            SalesInvoice updatedInvoice = repository.save(existingInvoice);
            kafkaProducerService.sendMessage(updatedInvoice);
            return updatedInvoice;
        } else {
            throw new RuntimeException("Sales Invoice with id " + id + " not found");
        }
    }

    @Transactional
    public void deleteSalesInvoice(Long id) {
        Optional<SalesInvoice> optionalInvoice = repository.findById(id);
        if (optionalInvoice.isPresent()) {
            repository.deleteById(id);

            SalesInvoice deletedInvoice = new SalesInvoice();
            deletedInvoice.setId(id);
            kafkaProducerService.sendMessage(deletedInvoice);
        } else {
            throw new RuntimeException("Sales invoice with id " + id + " not found");
        }
    }
}
