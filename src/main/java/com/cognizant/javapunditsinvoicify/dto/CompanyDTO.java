package com.cognizant.javapunditsinvoicify.dto;

import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {

    String name;

    AddressDTO addressDTO;

    String contactName;
    String contactTitle;
    String contactNumber;
    String invoices;

}
