package com.cognizant.javapunditsinvoicify.entity;

import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "invoiceEntity")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    Date createdDate;
    Date modifiedDate;
    String companyName;

    @OneToMany
    @JoinTable(
            name="invoiceItemEntity",
            joinColumns = @JoinColumn( name="invoice_id")
    )
    private List<InvoiceItemEntity> invoiceItemEntityList;

    @Enumerated(EnumType.ORDINAL)
    PaymentStatus paymentStatus;

    Double total;
}
