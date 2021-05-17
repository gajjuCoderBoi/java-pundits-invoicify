package com.cognizant.javapunditsinvoicify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CompanyDto {
    private String id;
    private String name;
    private AddressDto address;
    private String contactName;
    private String contactTitle;
    private Integer contactNumber;
    private List<InvoiceDto> invoices;

}