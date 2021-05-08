package com.cognizant.javapunditsinvoicify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDTO {

    String line1;
    String line2;
    String city;
    String state;
    Integer zip;
}
