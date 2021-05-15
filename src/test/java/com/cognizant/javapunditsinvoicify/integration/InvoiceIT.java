package com.cognizant.javapunditsinvoicify.integration;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.misc.FeeType;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.time.ZonedDateTime;

import static com.cognizant.javapunditsinvoicify.misc.PaymentStatus.PAID;
import static com.cognizant.javapunditsinvoicify.misc.PaymentStatus.UNPAID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("qa")
public class InvoiceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CompanyRepository companyRepository;

    private CompanyEntity companyEntity;
    private InvoiceEntity invoiceEntityPaid_Old;
    private InvoiceEntity invoiceEntityUnpaid;
    private InvoiceEntity invoiceEntityPaid_New;

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
        String companyId = createCompany();

        String invoiceId = objectMapper.readValue(postInvoice(companyId), ResponseMessage.class).getId();

        InvoiceItemDto sampleInvoiceItemDto = InvoiceItemDto.builder()
                .description("Test Item")
                .feeType(FeeType.RATE)
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
                                fieldWithPath("amount").description("Item Amount.  (Only Populate when feeType:rate) (Only Populate when feeType:flat)").type("Double").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").description("Unique Identifier of an Invoice Item.").type("Long"),
                                fieldWithPath("responseMessage").description("Response Message i.e Success Message or Error Message. ")
                        )
                ));
    }

    @Test
    public void postInvoiceTest() throws Exception {

        String companyId = createCompany();

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setPaymentStatus(PaymentStatus.UNPAID);

        RequestBuilder postInvoice = RestDocumentationRequestBuilders
                .post("/invoice/{companyId}", companyId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceDto));

        mockMvc.perform(postInvoice)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print())
                .andDo(document("add-invoice",
                        pathParameters(
                                parameterWithName("companyId").description("Company Id")
                        ),
                        requestFields(
                                fieldWithPath("createdDate").ignored(),
                                fieldWithPath("modifiedDate").ignored(),
                                fieldWithPath("paymentStatus").description("Payment Status.").type("String: PAID,UNPAID"),
                                fieldWithPath("total").ignored(),
                                fieldWithPath("items").ignored()
                        ),
                        responseFields(
                                fieldWithPath("id").description("Invoice Id."),
                                fieldWithPath("responseMessage").description("Response Message i.e Success Message or Error Message. ")
                        )
                ));
    }

    @Test
    public void getInvoiceById() throws Exception {
        String companyId = createCompany();
        String invoiceId = objectMapper.readValue(postInvoice(companyId), ResponseMessage.class).getId();
        addInvoiceItems(invoiceId);
        RequestBuilder getInvoiceById = RestDocumentationRequestBuilders.get("/invoice/{invoiceId}", invoiceId);
        mockMvc.perform(getInvoiceById)
                .andExpect(status().isOk())
                .andExpect(jsonPath("createdDate").exists())
                .andExpect(jsonPath("modifiedDate").exists())
                .andExpect(jsonPath("paymentStatus").exists())
                .andExpect(jsonPath("total").exists())
                .andExpect(jsonPath("items").exists())
                .andExpect(jsonPath("items").isArray())
                .andDo(print())
                .andDo(document("get-invoice-by-id", pathParameters(
                        parameterWithName("invoiceId").description("Invoice Id to return Invoice Details.")
                        ),
                        responseFields(
                                fieldWithPath("createdDate").description("Invoice Created Date and Time").type("String formatted Date"),
                                fieldWithPath("modifiedDate").description("Invoice Last updated Date and Time").type("String formatted Date"),
                                fieldWithPath("paymentStatus").description("Payment Status.").type("String: PAID,UNPAID"),
                                fieldWithPath("total").description("Total Billed Amount").type("Double"),
                                fieldWithPath("items.[]").description("An Array of Invoice Items.").type("Invoice Item"),
                                fieldWithPath("items.[].description").description("Item Description.").type("String"),
                                fieldWithPath("items.[].feeType").description("Type of the Item. ").type("String: FLAT, RATE"),
                                fieldWithPath("items.[].quantity").description("Quantity of An Item. (Only Populate when feeType:rate)").type("Integer").optional(),
                                fieldWithPath("items.[].rate").description("Rate of the Item. (Only Populate when feeType:rate)").type("Double").optional(),
                                fieldWithPath("items.[].amount").description("Item Amount.  (Only Populate when feeType:rate) (Only Populate when feeType:flat)").type("Double").optional()

                        )
                        )
                );
    }

    @Test
    public void updateInvoiceTest_Success() throws Exception {

        String companyId = createCompany();

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setPaymentStatus(PaymentStatus.UNPAID);

        RequestBuilder postInvoice = RestDocumentationRequestBuilders
                .post("/invoice/{companyId}", companyId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceDto));

        MvcResult result = mockMvc.perform(postInvoice)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print())
                .andReturn();

        String invoiceId = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class)
                .getId();

        InvoiceDto updatedInvoiceDto = new InvoiceDto();
        updatedInvoiceDto.setPaymentStatus(PAID);

        RequestBuilder updateInvoice = RestDocumentationRequestBuilders
                .put("/invoice/{invoiceId}", invoiceId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInvoiceDto));

        mockMvc.perform(updateInvoice)
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print())
                .andDo(document("update-invoice",
                        pathParameters(
                                parameterWithName("invoiceId").description("Invoice Id")
                        ),
                        requestFields(
                                fieldWithPath("createdDate").ignored(),
                                fieldWithPath("modifiedDate").ignored(),
                                fieldWithPath("paymentStatus").description("Payment Status.").type("String: PAID,UNPAID"),
                                fieldWithPath("total").ignored(),
                                fieldWithPath("items").ignored()

                        ),
                        responseFields(
                                fieldWithPath("id").description("Invoice Id."),
                                fieldWithPath("responseMessage").description("Response Message i.e Success Message or Error Message. ")
                        )
                ));
    }

    @Test
    public void updateInvoiceTest_Failure() throws Exception {

        String companyId = createCompany();

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setPaymentStatus(PAID);

        RequestBuilder postInvoice = RestDocumentationRequestBuilders
                .post("/invoice/{companyId}", companyId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceDto));

        MvcResult result = mockMvc.perform(postInvoice)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print())
                .andReturn();

        String invoiceId = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class)
                .getId();

        InvoiceDto updatedInvoiceDto = new InvoiceDto();
        updatedInvoiceDto.setPaymentStatus(PaymentStatus.UNPAID);

        RequestBuilder updateInvoice = RestDocumentationRequestBuilders
                .put("/invoice/{invoiceId}", invoiceId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedInvoiceDto));

        mockMvc.perform(updateInvoice)
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print());
    }

    @Test
    public void deleteInvoiceTest_Success() throws Exception {
        preSeedDB();

        RequestBuilder deleteInvoice = RestDocumentationRequestBuilders
                .delete("/invoice/{invoiceId}", invoiceEntityPaid_Old.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(deleteInvoice)
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print())
                .andDo(document("delete-invoice",
                        pathParameters(
                                parameterWithName("invoiceId").description("Invoice Id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Invoice Id."),
                                fieldWithPath("responseMessage").description("Response Message i.e Success Message or Error Message. ")
                        )
                ));
    }

    @Test
    public void deleteInvoiceTest_Failure_Unpaid() throws Exception {
        preSeedDB();

        RequestBuilder deleteInvoice = RestDocumentationRequestBuilders
                .delete("/invoice/{invoiceId}", invoiceEntityUnpaid.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(deleteInvoice)
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print());
    }

    @Test
    public void deleteInvoiceTest_Failure_Date() throws Exception {
        preSeedDB();

        RequestBuilder deleteInvoice = RestDocumentationRequestBuilders
                .delete("/invoice/{invoiceId}", invoiceEntityPaid_New.getId())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        mockMvc.perform(deleteInvoice)
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("responseMessage").exists())
                .andDo(print());
    }

    private String postInvoice(String companyId) throws Exception {

        InvoiceDto invoiceDto = new InvoiceDto();

        invoiceDto.setPaymentStatus(PaymentStatus.UNPAID);


        RequestBuilder postInvoice = post("/invoice/" + companyId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceDto));

        return mockMvc.perform(postInvoice)
                .andReturn().getResponse().getContentAsString();
    }

    private String createCompany() throws Exception {
        RequestBuilder createCompany = post("/company")
                .content(objectMapper.writeValueAsString(CompanyDto.builder()
                        .name(RandomStringUtils.randomAlphanumeric(10))
                        .contactName("Contact Name")
                        .contactTitle("Contact Title")
                        .contactNumber(123456789)
                        .address(AddressDto.builder()
                                .line1("Address line 1")
                                .line2("line 2")
                                .city("City")
                                .state("XX")
                                .zipcode(12343)
                                .build())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(createCompany)
                .andExpect(status().isCreated())
                .andReturn();

        String companyId = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class)
                .getId();

        return companyId;
    }

    private void addInvoiceItems(String invoiceId) throws Exception {

        mockMvc.perform(post("/invoice/item")
                .param("invoice_id", invoiceId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        InvoiceItemDto.builder()
                                .description("Test Item RATE")
                                .feeType(FeeType.RATE)
                                .quantity(5)
                                .rate(10.0d)
                                .build()
                )))
                .andExpect(status().isCreated())
                .andDo(print());

        mockMvc.perform(post("/invoice/item")
                .param("invoice_id", invoiceId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        InvoiceItemDto.builder()
                                .description("Test Item FLAT")
                                .feeType(FeeType.FLAT)
                                .amount(20D)
                                .build()
                )))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    private void preSeedDB(){
        companyEntity = new CompanyEntity();
        companyEntity.setName(RandomStringUtils.random(10));
        companyEntity = companyRepository.save(companyEntity);

        invoiceEntityPaid_Old = new InvoiceEntity();
        invoiceEntityPaid_Old.setCreatedDate(ZonedDateTime.now().minusYears(3));
        invoiceEntityPaid_Old.setModifiedDate(ZonedDateTime.now().minusYears(2));
        invoiceEntityPaid_Old.setPaymentStatus(PAID);
        invoiceEntityPaid_Old.setCompanyEntity(companyEntity);

        invoiceRepository.save(invoiceEntityPaid_Old);

        invoiceEntityUnpaid = new InvoiceEntity();
        invoiceEntityUnpaid.setCreatedDate(ZonedDateTime.now().minusYears(3));
        invoiceEntityUnpaid.setModifiedDate(ZonedDateTime.now().minusYears(2));
        invoiceEntityUnpaid.setPaymentStatus(UNPAID);
        invoiceEntityUnpaid.setCompanyEntity(companyEntity);

        invoiceRepository.save(invoiceEntityUnpaid);

        invoiceEntityPaid_New = new InvoiceEntity();
        invoiceEntityPaid_New.setCreatedDate(ZonedDateTime.now());
        invoiceEntityPaid_New.setModifiedDate(ZonedDateTime.now());
        invoiceEntityPaid_New.setPaymentStatus(PAID);
        invoiceEntityPaid_New.setCompanyEntity(companyEntity);

        invoiceRepository.save(invoiceEntityPaid_New);
    }


}
