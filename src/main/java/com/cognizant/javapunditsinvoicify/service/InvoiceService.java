package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.CREATED;

@Service
public class InvoiceService {
    public ResponseMessage addInvoiceItem(InvoiceItemDto invoiceItemDto, Long invoiceId) {
        return ResponseMessage.builder()
                .responseMessage("Invoice Item Successfully Added.")
                .httpStatus(CREATED)
                .build();
    }
}
