package com.cognizant.javapunditsinvoicify.repository;

import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
}
