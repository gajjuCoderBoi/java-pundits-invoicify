package com.cognizant.javapunditsinvoicify.dto;

import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sun.istack.NotNull;
import lombok.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(NON_NULL)
public class InvoiceDto {

    private Long id;
    private String createdDate;
    private String modifiedDate;
    private PaymentStatus paymentStatus;
    private Double total;
    private List<InvoiceItemDto> items;
    private CompanyDto company;

}
