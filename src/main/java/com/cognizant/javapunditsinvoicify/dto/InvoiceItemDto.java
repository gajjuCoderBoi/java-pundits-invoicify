package com.cognizant.javapunditsinvoicify.dto;

import com.cognizant.javapunditsinvoicify.misc.FeeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceItemDto {
    private String description;
    private Integer quantity;
    private Double amount;
    private FeeType feeType;
    private Double rate;
}
