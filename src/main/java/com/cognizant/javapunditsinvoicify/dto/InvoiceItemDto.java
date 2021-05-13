package com.cognizant.javapunditsinvoicify.dto;

import com.cognizant.javapunditsinvoicify.misc.FeeType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class InvoiceItemDto {
    private String description;
    private int quantity;
    private Double amount;
    private FeeType feeType;
    private Double rate;
}
