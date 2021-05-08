package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public void addCompany(CompanyDto companyDto)
    {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setName(companyDto.getName());
        AddressDto AddressDto = companyDto.getAddress();
        AddressEntity addressEntity = AddressEntity.builder()
                .line1(AddressDto.getLine1())
                .line2(AddressDto.getLine2())
                .city(AddressDto.getCity())
                .state(AddressDto.getState())
                .zip(AddressDto.getZipcode())
                .build();
        companyEntity.setAddressEntity(addressEntity);
        companyEntity.setContactName(companyDto.getContactName());
        companyEntity.setContactNumber(companyDto.getContactNumber());
        companyEntity.setContactTitle(companyDto.getContactTile());
        companyEntity.setInvoices(companyDto.getInvoices());
        this.companyRepository.save(companyEntity);
    }
}
