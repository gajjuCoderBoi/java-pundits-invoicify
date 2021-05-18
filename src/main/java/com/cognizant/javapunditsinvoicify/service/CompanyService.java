package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.mapper.AddressMapper;
import com.cognizant.javapunditsinvoicify.mapper.CompanyMapper;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.response.CompanySimpleViewResponse;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cognizant.javapunditsinvoicify.util.HelperMethods.isNotEmpty;

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

        Optional<CompanyEntity> companyExist = companyRepository.findAll().stream().filter(companyEntity -> companyEntity.getName().equals(companyDto.getName())).findFirst();

        if (companyExist.stream().count() ==0)
        {

            AddressDto AddressDto = companyDto.getAddress();
            AddressEntity addressEntity = new AddressEntity();

            addressEntity.setLine1(AddressDto.getLine1());
            addressEntity.setLine2(AddressDto.getLine2());
            addressEntity.setCity(AddressDto.getCity());
            addressEntity.setState(AddressDto.getState());
            addressEntity.setZip(AddressDto.getZipcode());

            CompanyEntity companyEntity = new CompanyEntity();
            companyEntity.setName(companyDto.getName());
            companyEntity.setAddressEntity(addressEntity);
            companyEntity.setContactName(companyDto.getContactName());
            companyEntity.setContactNumber(companyDto.getContactNumber());
            companyEntity.setContactTitle(companyDto.getContactTitle());

            companyEntity = companyRepository.save(companyEntity);

            if(companyEntity != null)
            {
                responseMessage.setId(companyEntity.getCompanyId().toString());
                responseMessage.setResponseMessage("Company created.");
            }
            else
            {
                responseMessage.setResponseMessage("Mock Company created");
            }
            responseMessage.setHttpStatus(HttpStatus.CREATED);

        }
        else {
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


    public List<CompanyDto> getCompanyList() {

        List<CompanyDto> companyListDto=new ArrayList<>();
        List<CompanyEntity> listSavedCompanyEntity =this.companyRepository.findAll();
    //    if(savedCompanyEntity. == null) return companyListDto;
        int i=0;
                for (CompanyEntity savedCompanyEntity:listSavedCompanyEntity){

         //       =this.companyRepository.findAll().stream().map(x->companyMapper.companyEntityToDto(x).setAddress(addressMapper.addressEntityToDto( x.getAddressEntity() )).collect(Collectors.toList());
                    CompanyDto  companyDto = companyMapper.companyEntityToDto(savedCompanyEntity);
                    companyDto.setAddress(addressMapper.addressEntityToDto(savedCompanyEntity.getAddressEntity()));
                    companyListDto.add(companyDto);
                }
        return companyListDto;
    }

    public List<CompanySimpleViewResponse> getCompanySimpleList() {
        List<CompanyEntity> savedCompanies=companyRepository.findAll();
        List<CompanySimpleViewResponse> listCompanySimpleViewResponse=savedCompanies.
                stream()
                .map(entity->{
                    return CompanySimpleViewResponse
                            .builder()
                            .companyName(entity.getName())
                            .city(entity.getAddressEntity().getCity())
                            .state(entity.getAddressEntity().getState())
                            .build();

                }).collect(Collectors.toList());
        return listCompanySimpleViewResponse;


    }
}

