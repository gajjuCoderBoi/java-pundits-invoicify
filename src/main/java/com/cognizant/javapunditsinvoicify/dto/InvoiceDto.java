package com.cognizant.javapunditsinvoicify.dto;

import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class InvoiceDto {

    private String createdDate;
    private String modifiedDate;
    private PaymentStatus paymentStatus;
    private Double total;
    private List<InvoiceItemDto> items;

}
