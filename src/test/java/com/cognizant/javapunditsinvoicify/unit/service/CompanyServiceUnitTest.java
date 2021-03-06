package com.cognizant.javapunditsinvoicify.unit.service;

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
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    private CompanyEntity sampleTestCompanyEntity;
    private CompanyDto sampleTestCompanyDto;
    private AddressDto addressDto;
    
    @BeforeEach
    void initMockData(){
        sampleTestCompanyEntity = new CompanyEntity();
        sampleTestCompanyEntity.setName("Name");
        sampleTestCompanyEntity.setContactName("Contact Name");
        sampleTestCompanyEntity.setContactTitle("Contact Title");
        sampleTestCompanyEntity.setContactNumber(1234567890L);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setLine1("Address Line 1");
        addressEntity.setLine2("Address Line 2");
        addressEntity.setCity("City");
        addressEntity.setState("XX");
        addressEntity.setZip(12345);

        sampleTestCompanyEntity.setAddressEntity(addressEntity);

        sampleTestCompanyDto = new CompanyDto();
        sampleTestCompanyDto.setName("Name");
        sampleTestCompanyDto.setContactName("Contact Name");
        sampleTestCompanyDto.setContactTitle("Contact Title");
        sampleTestCompanyDto.setContactNumber(1234567890L);

        addressDto = new AddressDto();
        addressDto.setLine1("Address Line 1");
        addressDto.setLine2("Address Line 2");
        addressDto.setCity("City");
        addressDto.setState("XX");
        addressDto.setZipcode(12345);

        sampleTestCompanyDto.setAddress(addressDto);
    }
    
    @Test
    public void getCompanyByIdTest(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(sampleTestCompanyEntity));
        when(companyMapper.companyEntityToDto(any())).thenReturn(sampleTestCompanyDto);
        when(addressMapper.addressEntityToDto(any())).thenReturn(addressDto);

        CompanyDto actualCompanyDto = companyService.getCompanyById(1L);

        assertNotNull(actualCompanyDto);
        assertEquals(actualCompanyDto.getName(), "Name");
        assertEquals(actualCompanyDto.getContactName(), "Contact Name");
        assertEquals(actualCompanyDto.getContactTitle(), "Contact Title");
        assertEquals(actualCompanyDto.getContactNumber(), 1234567890L);
        assertEquals(actualCompanyDto.getAddress().getLine1(), "Address Line 1");
        assertEquals(actualCompanyDto.getAddress().getLine2(), "Address Line 2");
        assertEquals(actualCompanyDto.getAddress().getCity(), "City");
        assertEquals(actualCompanyDto.getAddress().getState(), "XX");
        assertEquals(actualCompanyDto.getAddress().getZipcode(), 12345);
    }

    @Test
    public void updateCompanyTest(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(sampleTestCompanyEntity));
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(sampleTestCompanyEntity);

        companyService.update(sampleTestCompanyDto, 1L);
    }

    @Test
    public void updateCompanyTest_CompanyIdNotFound(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        companyService.update(sampleTestCompanyDto, 1L);
    }

    @Test
    public void updateCompanyTest_CompanyAddress(){

        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(sampleTestCompanyEntity));

        sampleTestCompanyDto.setAddress(new AddressDto());
        companyService.update(sampleTestCompanyDto, 1L);
    }

    @Test
    public void updateCompanyTest_NoCompanyAddress(){

        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(sampleTestCompanyEntity));

        sampleTestCompanyDto.setAddress(null);
        companyService.update(sampleTestCompanyDto, 1L);
    }

    @Test
    public void updateCompanyTest_NoAddressFound(){
        sampleTestCompanyEntity.setAddressEntity(null);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(sampleTestCompanyEntity));

        companyService.update(sampleTestCompanyDto, 1L);
    }

    @Test
    public void updateCompanyTest_NoNameAndContactName(){

        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(sampleTestCompanyEntity));

        sampleTestCompanyDto.setName("");
        sampleTestCompanyDto.setContactName("");
        companyService.update(sampleTestCompanyDto, 1L);
    }

    @Test
    void addCompany() {

        ResponseMessage actualResponse = companyService.addCompany(sampleTestCompanyDto);

        verify(companyRepository).save(sampleTestCompanyEntity);

        assertNotNull(actualResponse);
        assertNotNull(actualResponse.getResponseMessage());
        assertEquals(actualResponse.getResponseMessage(),"Mock Company created");
    }

    @Test
    public void getCompanyById(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(sampleTestCompanyEntity));
        CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class);
        AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
        CompanyDto companyDto = companyMapper.companyEntityToDto(sampleTestCompanyEntity);
        AddressDto addressDto = addressMapper.addressEntityToDto(sampleTestCompanyEntity.getAddressEntity());
        when(this.companyMapper.companyEntityToDto(any())).thenReturn(companyDto);
        when(this.addressMapper.addressEntityToDto(any())).thenReturn(addressDto);

        CompanyDto actualDto = companyService.getCompanyById(1L);

        assertNotNull(actualDto);
    }

    @Test
    public void getCompanyById_NoCompany(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        CompanyDto actualDto = companyService.getCompanyById(1L);

        assertNotNull(actualDto);
        assertNull(actualDto.getName());
        assertNull(actualDto.getAddress());
        assertNull(actualDto.getContactName());
        assertNull(actualDto.getContactNumber());
        assertNull(actualDto.getId());
        assertNull(actualDto.getInvoices());

    }
}
