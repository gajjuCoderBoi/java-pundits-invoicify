package com.cognizant.javapunditsinvoicify.controller;

import com.cognizant.javapunditsinvoicify.service.CompanyService;
import com.cognizant.javapunditsinvoicify.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoicify")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @PostMapping("/companies")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCompanies(@RequestBody CompanyDTO companyDTO)
    {
        companyService.addCompany(companyDTO);
    }
}
