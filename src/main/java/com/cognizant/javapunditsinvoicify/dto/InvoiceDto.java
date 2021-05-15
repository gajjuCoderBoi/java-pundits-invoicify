package com.cognizant.javapunditsinvoicify.dto;

import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class InvoiceDto {

    String createdDate;
    String modifiedDate;
    PaymentStatus paymentStatus;
    Double total;

}
