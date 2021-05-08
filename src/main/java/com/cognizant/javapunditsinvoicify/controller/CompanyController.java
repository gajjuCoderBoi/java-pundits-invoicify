package com.cognizant.javapunditsinvoicify.controller;

import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.CompanyService;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addCompanies(@RequestBody CompanyDto CompanyDto){
        ResponseMessage responseMessage = companyService.addCompany(CompanyDto);
        return new ResponseEntity<>(responseMessage,responseMessage.getHttpStatus());
    }

    @PutMapping("{company_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCompany(@PathVariable(name = "company_id") Long companyId,
                              @RequestBody CompanyDto companyDto) {
        companyService.update(companyDto,companyId) ;

    }
}
