package com.cognizant.javapunditsinvoicify.unit.service;

import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceItemMapper;
import com.cognizant.javapunditsinvoicify.repository.InvoiceItemRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private InvoiceItemRepository invoiceItemRepository;

    @Mock
    private InvoiceItemMapper invoiceItemMapper;

    @Test
    public void addInvoiceItem_Failed(){
        when(invoiceRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new InvoiceEntity()));
        when(invoiceItemMapper.invoiceItemDtoToEntity(any(InvoiceItemDto.class))).thenReturn(new InvoiceItemEntity());
        when(invoiceItemRepository.save(any(InvoiceItemEntity.class))).thenReturn(new InvoiceItemEntity());

        ResponseMessage actualResponse = invoiceService.addInvoiceItem(InvoiceItemDto.builder()
                .description("Test Item")
                .build(), 1L);

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getResponseMessage(), "Invoice Item Successfully Added.");
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

}