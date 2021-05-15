package com.cognizant.javapunditsinvoicify.unit.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import com.cognizant.javapunditsinvoicify.mapper.AddressMapper;
import com.cognizant.javapunditsinvoicify.mapper.CompanyMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceItemMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceMapper;
import com.cognizant.javapunditsinvoicify.misc.FeeType;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceItemRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.CompanyService;
import com.cognizant.javapunditsinvoicify.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("qa")
public class InvoiceServiceUnitTest {

    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private InvoiceItemRepository invoiceItemRepository;

    @Mock
    private InvoiceItemMapper invoiceItemMapper;

    private InvoiceItemDto invoiceItemDto;
    private InvoiceDto invoiceDto;
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
        invoiceDto = new InvoiceDto();
        invoiceDto.setPaymentStatus(PaymentStatus.UNPAID);
        invoiceDto.setTotal(100.0d);

        //Company Mock data
        mockCompanyEntity = new CompanyEntity();
        mockCompanyEntity.setName("Name");
        mockCompanyEntity.setContactName("Contact Name");
        mockCompanyEntity.setContactTitle("Contact Title");
        mockCompanyEntity.setContactNumber(123456789);

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

        addressDto = new AddressDto();
        addressDto.setLine1("Address Line 1");
        addressDto.setLine2("Address Line 2");
        addressDto.setCity("City");
        addressDto.setState("XX");
        addressDto.setZipcode(12345);

        mockCompanyDto.setAddress(addressDto);
    }

    @Test
    public void addInvoiceItem_Failed(){
        when(invoiceRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new InvoiceEntity()));
        when(invoiceItemMapper.invoiceItemDtoToEntity(any(InvoiceItemDto.class))).thenReturn(new InvoiceItemEntity());
        when(invoiceItemRepository.save(any(InvoiceItemEntity.class))).thenReturn(new InvoiceItemEntity());

        ResponseMessage actualResponse = invoiceService.addInvoiceItem(InvoiceItemDto.builder()
                .description("Test Item")
                .build(), 1L);

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getResponseMessage(), "InvoiceItem added Successfully.");
        assertEquals(actualResponse.getHttpStatus(), CREATED);

    }

    @Test
    public void addInvoiceItem_Success(){
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseMessage actualResponse = invoiceService.addInvoiceItem(InvoiceItemDto.builder()
                .description("Test Item")
                .build(), 1L);

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getResponseMessage(), "Invalid Invoice Id. Not Found.");
        assertEquals(actualResponse.getHttpStatus(), NOT_FOUND);

    }

    @Test
    public void createInvoices()
    {
//Create a company

        ResponseMessage actualResponseCompany = companyService.addCompany(mockCompanyDto);

        verify(companyRepository).save(mockCompanyEntity);

        assertNotNull(actualResponseCompany);
        assertNotNull(actualResponseCompany.getResponseMessage());
        assertEquals(actualResponseCompany.getResponseMessage(),"Mock Company created");
        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockCompanyEntity));

        when(invoiceMapper.invoiceDtoToEntity(any())).thenReturn(new InvoiceEntity());
        ResponseMessage actualResponse = invoiceService.addInvoice(invoiceDto,1l);

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getHttpStatus(), CREATED);
    }

}
