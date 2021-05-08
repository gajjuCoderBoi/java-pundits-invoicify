package com.cognizant.javapunditsinvoicify.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public void addCompany(CompanyDTO companyDTO)
    {
        Company company = new Company();
        company.setName(companyDTO.getName());
        company.setAddress(companyDTO.getAddress());
        company.setContactName(companyDTO.getContactName());
        company.setContactNumber(companyDTO.getContactNumber());
        company.setContactTitle(companyDTO.getContactTitle());
        company.setInvoices(companyDTO.getInvoices());
        this.companyRepository.save(company);
    }
}
