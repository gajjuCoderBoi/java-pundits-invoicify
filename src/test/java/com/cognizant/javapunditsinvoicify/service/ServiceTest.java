package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService service;

    @Test
    void addCompany() {
        AddressDto addressDto = new AddressDto("Address line 1", "line 2", "City", "XX", 12345);

        CompanyDto companyDto = new CompanyDto("First Company", addressDto, "Contact Name", "Contact Title", 123456789, "Invoices");

        AddressEntity addressEntity =  AddressEntity.builder().id(12345L)
                .line1("Address line 1").line2("line 2").city("City").state("XX").zip(12345).build();

        CompanyEntity companyEntity = CompanyEntity.builder()
                .id(123456L)
                .name("First Company").addressEntity(addressEntity).contactName("Contact Name")
                .contactTitle("Contact Title").contactNumber(123456789).invoices("Invoices").build();

        ResponseMessage responseMessage = service.addCompany(companyDto);

        //when(companyRepository).save(companyEntity).thenReturn(new CompanyEntity("First Company",addressEntity,"Contact Name", "Contact Title", 123456789, "Invoices"));

        verify(companyRepository).save(companyEntity);



        assertNotNull(responseMessage);
        assertNotNull(responseMessage.getResponseMessage());
    }

}
