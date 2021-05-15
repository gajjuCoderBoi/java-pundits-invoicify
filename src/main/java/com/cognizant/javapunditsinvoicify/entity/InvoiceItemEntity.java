package com.cognizant.javapunditsinvoicify.entity;

import ch.qos.logback.classic.db.names.TableName;
import com.cognizant.javapunditsinvoicify.misc.FeeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "invoiceItemEntity")
public class InvoiceItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;
    private int quantity;
    private Double amount;

    @Enumerated(EnumType.ORDINAL)
    private FeeType feeType;

    private Double rate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private InvoiceEntity invoiceEntity;

}
