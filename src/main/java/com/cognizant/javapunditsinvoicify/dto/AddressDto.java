package com.cognizant.javapunditsinvoicify.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AddressDto {
    private String line1;
    private String line2;
    private String city;
    private String state;
    private Integer zipcode;
}
