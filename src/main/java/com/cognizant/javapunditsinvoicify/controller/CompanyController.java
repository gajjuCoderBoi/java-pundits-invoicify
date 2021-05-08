package com.cognizant.javapunditsinvoicify.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @PutMapping("{company_id}")
    public void updateCompany(@PathVariable(name = "company_id") Long companyId) {
        return ;

    }

}
