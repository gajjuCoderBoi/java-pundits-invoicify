package com.cognizant.javapunditsinvoicify.integration;

import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.MethodMode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@DirtiesContext(methodMode = BEFORE_METHOD)
@ActiveProfiles("qa")
public class InvoiceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postInvoiceItem_Success() throws Exception {

        InvoiceItemDto sampleInvoiceItemDto = InvoiceItemDto.builder()
                .description("Test Item")
                .build();

        RequestBuilder postInvoiceItem = post("/invoice")
                .param("invoice_id", "1")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleInvoiceItemDto))
                ;

        mockMvc.perform(postInvoiceItem)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").value("Invoice Item Successfully Added."))
                .andDo(print())
        ;
    }
}
