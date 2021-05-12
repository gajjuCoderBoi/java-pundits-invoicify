package com.cognizant.javapunditsinvoicify.unit.controller;

import com.cognizant.javapunditsinvoicify.controller.InvoiceController;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
@ActiveProfiles("qa")
public class InvoiceControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private InvoiceService invoiceService;

    @Test
    public void postInvoiceItem_Failed_InvalidInvoiceId() throws Exception {
        InvoiceItemDto sampleInvoiceItemDto = InvoiceItemDto.builder()
                .description("Test Item")
                .build();

        RequestBuilder postInvoiceItem = post("/invoice/item")
                .param("invoice_id", "1")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(sampleInvoiceItemDto))
                ;

        when(invoiceService.addInvoiceItem(any(InvoiceItemDto.class), anyLong())).thenReturn(ResponseMessage.builder()
                .responseMessage("Invalid Invoice Id. Not Found.")
                .httpStatus(NOT_FOUND)
                .build());

        mockMvc.perform(postInvoiceItem)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("responseMessage").value("Invalid Invoice Id. Not Found."))
                .andDo(print())
        ;
    }

    @Test
    public void postInvoiceItem_Success() throws Exception {

        InvoiceItemDto sampleInvoiceItemDto = InvoiceItemDto.builder()
                .description("Test Item")
                .build();

        RequestBuilder postInvoiceItem = post("/invoice/item")
                .param("invoice_id", "1")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(sampleInvoiceItemDto))
                ;

        when(invoiceService.addInvoiceItem(any(InvoiceItemDto.class), anyLong())).thenReturn(ResponseMessage.builder()
                .responseMessage("Invoice Item Successfully Added.")
                .httpStatus(CREATED)
                .build());

        mockMvc.perform(postInvoiceItem)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").value("Invoice Item Successfully Added."))
                .andDo(print())
        ;
    }
}
