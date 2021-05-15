package com.cognizant.javapunditsinvoicify.unit;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.mapper.AddressMapper;
import com.cognizant.javapunditsinvoicify.mapper.CompanyMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceMapper;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.cognizant.javapunditsinvoicify.service.CompanyService;
import com.cognizant.javapunditsinvoicify.service.InvoiceService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("qa")
public class InvoiceServiceUnitTest {
    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    private InvoiceEntity mockInvoiceEntity;
    private CompanyDto mockCompanyDto;
    private AddressDto addressDto;



}
