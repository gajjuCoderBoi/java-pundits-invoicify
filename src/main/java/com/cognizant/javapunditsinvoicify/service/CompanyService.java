package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.mapper.AddressMapper;
import com.cognizant.javapunditsinvoicify.mapper.CompanyMapper;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    @Qualifier("address-mapper")
    private AddressMapper addressMapper ;

    @Autowired
    @Qualifier("company-mapper")
    private CompanyMapper companyMapper;
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
            companyEntity.setContactTitle(companyDto.getContactTitle());
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
        if (companyDto.getAddress() != null) {
            AddressDto addressDto = companyDto.getAddress();
            AddressEntity savedCompanyAddressEntity = savedCompanyEntity.getAddressEntity();
            if (savedCompanyAddressEntity == null) savedCompanyAddressEntity = new AddressEntity();
            if(isNotEmpty(addressDto.getLine1())) savedCompanyAddressEntity.setLine1(addressDto.getLine1());
            if(isNotEmpty(addressDto.getLine2())) savedCompanyAddressEntity.setLine2(addressDto.getLine2());
            if(isNotEmpty(addressDto.getCity())) savedCompanyAddressEntity.setCity(addressDto.getCity());
            if(isNotEmpty(addressDto.getState())) savedCompanyAddressEntity.setState(addressDto.getState());
            if (addressDto.getZipcode() != null) savedCompanyAddressEntity.setZip(addressDto.getZipcode());
            savedCompanyEntity.setAddressEntity(savedCompanyAddressEntity);
        }

        if(isNotEmpty(companyDto.getName())) savedCompanyEntity.setName(companyDto.getName());
        if(isNotEmpty(companyDto.getContactName())) savedCompanyEntity.setContactName(companyDto.getContactName());
        if(isNotEmpty(companyDto.getContactTitle())) savedCompanyEntity.setContactTitle(companyDto.getContactTitle());
        if(companyDto.getContactNumber()!=null) savedCompanyEntity.setContactNumber(companyDto.getContactNumber());
        companyRepository.save(savedCompanyEntity);
    }

    public CompanyDto getCompanyById(Long companyId) {
        CompanyEntity savedCompanyEntity = companyRepository.findById(companyId).orElse(null);
        if(savedCompanyEntity == null) return new CompanyDto();
        CompanyDto companyDto = companyMapper.companyEntityToDto(savedCompanyEntity);
        companyDto.setAddress(addressMapper.addressEntityToDto(savedCompanyEntity.getAddressEntity()));
        return companyDto;
    }

    private boolean isNotEmpty(String value){
        return StringUtils.isNotEmpty(value) && StringUtils.isNotBlank(value);
    }

    public List<CompanyDto> getCompanyList() {
        List<CompanyDto> companyListDto=this.companyRepository.findAll().stream().map(x->companyMapper.companyEntityToDto(x)).collect(Collectors.toList());
        return companyListDto;
    }
}

