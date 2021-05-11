package com.cognizant.javapunditsinvoicify.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanySimpleViewResponse {


    private String companyName;
    private String city;
    private String state;


}
