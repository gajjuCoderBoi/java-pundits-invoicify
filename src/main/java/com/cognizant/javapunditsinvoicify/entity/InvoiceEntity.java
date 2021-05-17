package com.cognizant.javapunditsinvoicify.entity;

import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;

@Entity
@Data
@NoArgsConstructor
@Table(name = "invoices")
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ZonedDateTime createdDate;
    private ZonedDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name="company_id", referencedColumnName = "companyId")
    private CompanyEntity companyEntity;

    @OneToMany(mappedBy = "invoiceEntity", cascade = MERGE)
    private List<InvoiceItemEntity> invoiceItemEntityList;

    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus paymentStatus;

}
