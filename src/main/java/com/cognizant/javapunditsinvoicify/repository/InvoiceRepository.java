package com.cognizant.javapunditsinvoicify.repository;

import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    List<InvoiceEntity> findInvoiceEntitiesByCompanyEntityAndPaymentStatusEquals(CompanyEntity savedCompanyEntity, PaymentStatus paymentStatus);
}
