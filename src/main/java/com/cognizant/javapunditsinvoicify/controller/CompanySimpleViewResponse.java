package com.cognizant.javapunditsinvoicify.controller;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CompanySimpleViewResponse {

    private String companyName;
    private String city;
    private String state;


}
