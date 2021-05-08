package com.cognizant.javapunditsinvoicify.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CompanyDto {
    private String name;
    private AddressDto address;
    private String contactName;
    private String contactTile;
    private Integer contactNumber;
    private String invoices;

}