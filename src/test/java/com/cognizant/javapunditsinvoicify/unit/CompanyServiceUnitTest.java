package com.cognizant.javapunditsinvoicify.unit;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.mapper.AddressMapper;
import com.cognizant.javapunditsinvoicify.mapper.CompanyMapper;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("qa")
public class CompanyServiceUnitTest {
    @InjectMocks
    private CompanyService companyService;
    
    @Mock
    private CompanyRepository companyRepository;
    
    @Mock
    private CompanyMapper companyMapper;

    @Mock
    private AddressMapper addressMapper;
    
    private CompanyEntity mockCompanyEntity;
    private CompanyDto mockCompanyDto;
    private AddressDto addressDto;
    
    @BeforeEach
    void initMockData(){
        mockCompanyEntity = new CompanyEntity();
        mockCompanyEntity.setName("Name");
        mockCompanyEntity.setContactName("Contact Name");
        mockCompanyEntity.setContactTitle("Contact Title");
        mockCompanyEntity.setContactNumber(123456789);
        mockCompanyEntity.setInvoices("Invoices");

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setLine1("Address Line 1");
        addressEntity.setLine2("Address Line 2");
        addressEntity.setCity("City");
        addressEntity.setState("XX");
        addressEntity.setZip(12345);

        mockCompanyEntity.setAddressEntity(addressEntity);

        mockCompanyDto = new CompanyDto();
        mockCompanyDto.setName("Name");
        mockCompanyDto.setContactName("Contact Name");
        mockCompanyDto.setContactTitle("Contact Title");
        mockCompanyDto.setContactNumber(123456789);
        mockCompanyDto.setInvoices("Invoices");

        addressDto = new AddressDto();
        addressDto.setLine1("Address Line 1");
        addressDto.setLine2("Address Line 2");
        addressDto.setCity("City");
        addressDto.setState("XX");
        addressDto.setZipcode(12345);

        mockCompanyDto.setAddress(addressDto);
    }
    
    @Test
    public void getCompanyByIdTest(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockCompanyEntity));
        when(companyMapper.companyEntityToDto(any())).thenReturn(mockCompanyDto);
        when(addressMapper.addressEntityToDto(any())).thenReturn(addressDto);

        CompanyDto actualCompanyDto = companyService.getCompanyById(1L);

        assertNotNull(actualCompanyDto);
        assertEquals(actualCompanyDto.getName(), "Name");
        assertEquals(actualCompanyDto.getContactName(), "Contact Name");
        assertEquals(actualCompanyDto.getContactTitle(), "Contact Title");
        assertEquals(actualCompanyDto.getContactNumber(), 123456789);
        assertEquals(actualCompanyDto.getAddress().getLine1(), "Address Line 1");
        assertEquals(actualCompanyDto.getAddress().getLine2(), "Address Line 2");
        assertEquals(actualCompanyDto.getAddress().getCity(), "City");
        assertEquals(actualCompanyDto.getAddress().getState(), "XX");
        assertEquals(actualCompanyDto.getAddress().getZipcode(), 12345);
    }

    @Test
    public void updateCompanyTest(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockCompanyEntity));
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(mockCompanyEntity);

        companyService.update(mockCompanyDto, 1L);
    }


    @Test
    void addCompany() {
        AddressDto addressDto = new AddressDto("Address line 1", "line 2", "City", "XX", 12345);

        CompanyDto companyDto = new CompanyDto("First Company", addressDto, "Contact Name", "Contact Title", 123456789, "Invoices");


        AddressEntity addressEntity = new AddressEntity("Address line 1","line 2","City","XX",12345);

        CompanyEntity companyEntity = new CompanyEntity("First Company", addressEntity,"Contact Name" ,"Contact Title",123456789,"Invoices");

        ResponseMessage responseMessage = service.addCompany(companyDto);

        //when(companyRepository).save(companyEntity).thenReturn(new CompanyEntity("First Company",addressEntity,"Contact Name", "Contact Title", 123456789, "Invoices"));

        when(companyRepository.save(companyEntity)).thenReturn(new ResponseMessage(companyEntity.getId().toString(), HttpStatus.CREATED));

        assertNotNull(responseMessage);
        assertNotNull(responseMessage.getResponseMessage());
    }
}
