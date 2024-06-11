package gityeong.SalesInvoice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_invoice")
public class SalesInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_name", nullable = false)
    @JsonProperty("customer_name")
    private String customerName;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "invoice_date", nullable = false)
    @JsonProperty("invoice_date")
    private LocalDate invoiceDate;

    //Default Constructor
    public SalesInvoice(){
    }

    public SalesInvoice(Long id, String customerName, BigDecimal amount, LocalDate invoiceDate) {
        this.id = id;
        this.customerName = customerName;
        this.amount = amount;
        this.invoiceDate = invoiceDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Override
    public String toString() {
        return "SalesInvoice [id=" + id + ", customerName=" + customerName + ", amount=" + amount + ", invoiceDate="
                + invoiceDate + "]";
    }
}
