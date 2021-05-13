package com.cognizant.javapunditsinvoicify.integration;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.misc.FeeType;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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
    public void postInvoiceItem_Failed_InvalidInvoiceId() throws Exception {

        InvoiceItemDto sampleInvoiceItemDto = InvoiceItemDto.builder()
                .description("Test Item")
                .build();

        RequestBuilder postInvoiceItem = post("/invoice/item")
                .param("invoice_id", "1")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleInvoiceItemDto));

        mockMvc.perform(postInvoiceItem)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("responseMessage").value("Invalid Invoice Id. Not Found."))
                .andDo(print())
        ;
    }

    @Test
    public void postInvoiceItem_Success() throws Exception {
        String invoiceId = postInvoice();

        InvoiceItemDto sampleInvoiceItemDto = InvoiceItemDto.builder()
                .description("Test Item")
                .feeType(FeeType.FLAT)
                .quantity(5)
                .rate(10.0d)
                .build();

        RequestBuilder postInvoiceItem = post("/invoice/item")
                .param("invoice_id", invoiceId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleInvoiceItemDto));

        mockMvc.perform(postInvoiceItem)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print())
                .andDo(document("invoice-add-item",
                        requestParameters(
                                parameterWithName("invoice_id").description("Invoice Id to add Item into.")
                        ),
                        requestFields(
                                fieldWithPath("description").description("Item Description.").type("String"),
                                fieldWithPath("feeType").description("Type of the Item. ").type("String: FLAT, RATE"),
                                fieldWithPath("quantity").description("Quantity of An Item. (Only Populate when feeType:rate)").type("Integer"),
                                fieldWithPath("rate").description("Rate of the Item. (Only Populate when feeType:rate)").type("Double"),
                                fieldWithPath("amount").description("Item Amount.  (Only Populate when feeType:rate) (Only Populate when feeType:flat)").type("Double")
                                ),
                        responseFields(
                                fieldWithPath("responseMessage").description("Response Message i.e Success Message or Error Message. ")
                        )
                ));
  }

    private String postInvoice() throws Exception {

        InvoiceDto invoiceDto = new InvoiceDto();
        Date date = new Date(System.currentTimeMillis());
        invoiceDto.setCreatedDate(date);
        invoiceDto.setModifiedDate(date);

        InvoiceItemDto invoiceItemDto = new InvoiceItemDto();
        invoiceItemDto.setDescription("Item 1");
        invoiceItemDto.setFeeType(FeeType.RATE);
        invoiceItemDto.setRate(5.0d);
        invoiceItemDto.setQuantity(5);
        invoiceItemDto.setDescription("Item 1");
        List<InvoiceItemDto> itemListDto = new ArrayList<>();
        itemListDto.add(invoiceItemDto);

        invoiceDto.setInvoiceItemDtoList(itemListDto);
        invoiceDto.setCompanyName("Test Company");

        invoiceDto.setPaymentStatus(PaymentStatus.UNPAID);
        invoiceDto.setTotal(100.0d);

        RequestBuilder postInvoice = post("/invoice")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceDto));

        return mockMvc.perform(postInvoice)
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void postInvoiceTest() throws Exception {
        InvoiceDto invoiceDto = new InvoiceDto();
        Date date = new Date(System.currentTimeMillis());
        invoiceDto.setCreatedDate(date);
        invoiceDto.setModifiedDate(date);

        InvoiceItemDto invoiceItemDto = new InvoiceItemDto();
        invoiceItemDto.setDescription("Item 1");
        invoiceItemDto.setFeeType(FeeType.RATE);
        invoiceItemDto.setRate(5.0d);
        invoiceItemDto.setQuantity(5);
        List<InvoiceItemDto> itemListDto = new ArrayList<>();
        itemListDto.add(invoiceItemDto);

        invoiceDto.setInvoiceItemDtoList(itemListDto);
        invoiceDto.setCompanyName("Test Company");

        invoiceDto.setPaymentStatus(PaymentStatus.UNPAID);
        invoiceDto.setTotal(100.0d);

        RequestBuilder postInvoice = post("/invoice")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceDto));

        mockMvc.perform(postInvoice)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print());
//                .andDo(document("add-invoice",
//                        requestParameters(
//                                parameterWithName("invoice_id").description("Invoice Id to add Item into.")
//                        ),
//                        requestFields(
//                                fieldWithPath("description").description("Item Description.").type("String"),
//                                fieldWithPath("feeType").description("Type of the Item. ").type("String: FLAT, RATE"),
//                                fieldWithPath("quantity").description("Quantity of An Item. (Only Populate when feeType:rate)").type("Integer"),
//                                fieldWithPath("rate").description("Rate of the Item. (Only Populate when feeType:rate)").type("Double"),
//                                fieldWithPath("amount").description("Item Amount.  (Only Populate when feeType:rate) (Only Populate when feeType:flat)").type("Double")
//                        ),
//                        responseFields(
//                                fieldWithPath("responseMessage").description("Response Message i.e Success Message or Error Message. ")
//                        )
//                ));
    }
}
