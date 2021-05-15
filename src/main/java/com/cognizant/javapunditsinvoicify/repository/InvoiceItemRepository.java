package com.cognizant.javapunditsinvoicify.repository;

import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItemEntity, Long> {

}
