package com.cognizant.javapunditsinvoicify.unit.controller;

import com.cognizant.javapunditsinvoicify.controller.InvoiceController;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.InvoiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.cognizant.javapunditsinvoicify.misc.PaymentStatus.UNPAID;
import static com.cognizant.javapunditsinvoicify.util.DateFormatUtil.formatDate;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .responseMessage("InvoiceItem added Successfully.")
                .httpStatus(CREATED)
                .build());

        mockMvc.perform(postInvoiceItem)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").value("InvoiceItem added Successfully."))
                .andDo(print())
        ;
    }

    @Test
    public void getInvoiceByIdUnitTest() throws Exception {
        ZonedDateTime createdTime =  ZonedDateTime.now();
        ZonedDateTime modifiedTime = ZonedDateTime.now();
        InvoiceDto mockInvoiceDto = InvoiceDto.builder()
                .total(100D)
                .createdDate(formatDate(createdTime))
                .build();

        RequestBuilder  getInvoiceById = get("/invoice/1")
                .accept(APPLICATION_JSON);

        when(invoiceService.getInvoiceById(anyLong())).thenReturn(mockInvoiceDto);

        mockMvc.perform(getInvoiceById)
                .andExpect(status().isOk());

    }

    @Test
    public void getCompanyInvoices_Unpaid_Success() throws Exception {
        List<InvoiceDto> mockUnpaidInvoices = new ArrayList<>();
        int unpaidCount = 10;
        for (int i=1;i<=10;i++){
            mockUnpaidInvoices.add(InvoiceDto.builder()
                    .paymentStatus(UNPAID)
                    .build());
        }

        when(invoiceService.getCompanyUnpaidInvoices(anyLong())).thenReturn(mockUnpaidInvoices);

        RequestBuilder getCompanyInvoices_Unpaid_Success= RestDocumentationRequestBuilders.get("/invoice/{companyId}/unpaid", "1");
        mockMvc.perform(getCompanyInvoices_Unpaid_Success)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$",hasSize(unpaidCount)))
                .andDo(print());



    }

    @Test
    public void updateInvoiceTest() throws Exception {

        String invoiceId = "1";
        var invoiceDto = new InvoiceDto();
        invoiceDto.setPaymentStatus(PaymentStatus.PAID);

        RequestBuilder updatedInvoice = put(String.format("/invoice/%s", invoiceId))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(invoiceDto));

        System.out.println("Request Builder :" + updatedInvoice.toString());

        when(invoiceService.updateInvoice(any(InvoiceDto.class), anyLong())).thenReturn(ResponseMessage.builder()
                .responseMessage("Update successfully.")
                .httpStatus(CREATED)
                .build());


        mockMvc.perform(updatedInvoice)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").value("Update successfully."))
                .andDo(print());
    }


    @Test
    public void deleteInvoiceTest() throws Exception {

        String invoiceId = "1";

        RequestBuilder deletedInvoice = delete(String.format("/invoice/%s", invoiceId))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        when(invoiceService.deleteInvoice(anyLong())).thenReturn(ResponseMessage.builder()
                .responseMessage("Invoice deleted successfully.")
                .httpStatus(ACCEPTED)
                .build());


        mockMvc.perform(deletedInvoice)
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("responseMessage").value("Invoice deleted successfully."))
                .andDo(print());
    }


}
