package com.cognizant.javapunditsinvoicify.controller;

import com.cognizant.javapunditsinvoicify.service.CompanyService;
import com.cognizant.javapunditsinvoicify.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void addCompanies(@RequestBody CompanyDTO companyDTO){
        companyService.addCompany(companyDTO);
    }

    @PutMapping("{company_id}")
    public void updateCompany(@PathVariable(name = "company_id") Long companyId) {
        return ;

    }
}
