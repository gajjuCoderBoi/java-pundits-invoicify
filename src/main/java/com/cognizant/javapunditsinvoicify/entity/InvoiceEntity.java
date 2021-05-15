package com.cognizant.javapunditsinvoicify.entity;

import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name="company_id", referencedColumnName = "id")
    private CompanyEntity companyEntity;

    @OneToMany(mappedBy = "invoiceEntity")
    private List<InvoiceItemEntity> invoiceItemEntityList;

    @Enumerated(EnumType.ORDINAL)
    private PaymentStatus paymentStatus;

}
