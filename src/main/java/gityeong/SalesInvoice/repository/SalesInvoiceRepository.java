package gityeong.SalesInvoice.repository;


import org.springframework.data.domain.Sort;
import gityeong.SalesInvoice.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Long> {
}
