package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDTO;
import com.cognizant.javapunditsinvoicify.dto.CompanyDTO;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public void addCompany(CompanyDTO companyDTO)
    {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setName(companyDTO.getName());
        AddressDTO addressDTO = companyDTO.getAddressDTO();
        AddressEntity addressEntity = AddressEntity.builder()
                .line1(addressDTO.getLine1())
                .line2(addressDTO.getLine2())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .zip(addressDTO.getZip())
                .build();
        companyEntity.setAddressEntity(addressEntity);
        companyEntity.setContactName(companyDTO.getContactName());
        companyEntity.setContactNumber(companyDTO.getContactNumber());
        companyEntity.setContactTitle(companyDTO.getContactTitle());
        companyEntity.setInvoices(companyDTO.getInvoices());
        this.companyRepository.save(companyEntity);
    }
}
