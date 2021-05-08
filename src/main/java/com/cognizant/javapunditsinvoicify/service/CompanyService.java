package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.mapper.AddressMapper;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    @Qualifier("address-mapper")
    private AddressMapper addressMapper ;

    public ResponseMessage addCompany(CompanyDto companyDto) {

        ResponseMessage responseMessage = new ResponseMessage();

        Optional<CompanyEntity> companyExist = companyRepository.findAll().stream().filter(companyEntity -> companyEntity.getName().equals(companyDto.getName())).findAny();

        if (companyExist.stream().count() ==0) {
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
            companyEntity = this.companyRepository.save(companyEntity);
            responseMessage.setResponseMessage(companyEntity.getId().toString());
            responseMessage.setHttpStatus(HttpStatus.CREATED);

        } else {
            responseMessage.setResponseMessage("Company Already Exist");
            responseMessage.setHttpStatus(HttpStatus.CONFLICT);
        }
        return responseMessage;

    }

    public void update(CompanyDto companyDto, Long companyId) {
        CompanyEntity savedCompanyEntity;

        savedCompanyEntity = companyRepository.findById(companyId).orElse(null);
        if (savedCompanyEntity == null) return;
        if (companyDto.getAddress() != null)
            savedCompanyEntity.setAddressEntity(addressMapper.addressDtoToEntity(companyDto.getAddress()));
        savedCompanyEntity.setName(companyDto.getName());
        savedCompanyEntity.setContactName(companyDto.getContactName());
        savedCompanyEntity.setContactTitle(companyDto.getContactTile());
        savedCompanyEntity.setContactNumber(companyDto.getContactNumber());
        companyRepository.save(savedCompanyEntity);

    }
}

