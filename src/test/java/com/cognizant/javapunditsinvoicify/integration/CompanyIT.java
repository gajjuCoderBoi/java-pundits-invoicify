package com.cognizant.javapunditsinvoicify.integration;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("qa")
public class CompanyIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * @author Raghavendra, Koustav
     */

    @Test
    public void createCompanyTest() throws Exception {

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Name");
        companyDto.setContactName("Contact Name");
        companyDto.setContactTitle("Contact Title");
        companyDto.setContactNumber(1234567890L);

        AddressDto addressDto = new AddressDto();
        addressDto.setLine1("Address line 1");
        addressDto.setLine2("line 2");
        addressDto.setCity("City");
        addressDto.setState("XX");
        addressDto.setZipcode(12345);

        companyDto.setAddress(addressDto);

        RequestBuilder rq = post("/company")
                .content(objectMapper.writeValueAsString(companyDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("addCompany", requestFields(
                        fieldWithPath("name").description("Name of the Company"),
                        fieldWithPath("contactName").description("Name of the Contact person of the company"),
                        fieldWithPath("contactTitle").description("Title of the Contact person of the company"),
                        fieldWithPath("contactNumber").description("Contact No of the company PoC"),
                        fieldWithPath("address.line1").description("Address line 1 of the Company"),
                        fieldWithPath("address.line2").description("Address line 2 of the Company"),
                        fieldWithPath("address.city").description("City of the Company location"),
                        fieldWithPath("address.state").description("State of the Company location"),
                        fieldWithPath("address.zipcode").description("Zip-Code of the Company location")

                )))
                .andDo(document("addCompany", responseFields(
                        fieldWithPath("id").description("Id of the company created."),
                        fieldWithPath("responseMessage").description("Company created or conflict error-message for payload")
                )));

    }

    /**
     * @author Raghavendra, Koustav
     * To validate the dupe entries are not allowed
     */

    @Test
    public void createDuplicateCompanyTest() throws Exception{

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("First Company");
        companyDto.setContactName("Contact Name");
        companyDto.setContactTitle("Contact Title");
        companyDto.setContactNumber(1234567890L);

        AddressDto addressDto = new AddressDto();
        addressDto.setLine1("Address line 1");
        addressDto.setLine2("line 2");
        addressDto.setCity("City");
        addressDto.setState("XX");
        addressDto.setZipcode(12345);

        companyDto.setAddress(addressDto);

        RequestBuilder rq = post("/company")
                .content(objectMapper.writeValueAsString(companyDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("responseMessage").exists());

        mockMvc.perform(rq)
                .andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("responseMessage").value("Company Already Exist"));

    }


    /**
     * @author Mohammad Javed, Jose Antonio
     */
    @Test
    @DirtiesContext()
    public void updateCompany_Success() throws Exception {
        RequestBuilder createCompany = post("/company")
                .content(objectMapper.writeValueAsString(CompanyDto.builder()
                        .name("Test Name")
                        .contactName("Contact Name")
                        .contactTitle("Contact Title")
                        .contactNumber(1234567890L)
                        .address(AddressDto.builder()
                                .line1("Address line 1")
                                .line2("line 2")
                                .city("City")
                                .state("XX")
                                .zipcode(12343)
                                .build())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result=  mockMvc.perform(createCompany)
                .andExpect(status().isCreated())
                .andReturn()
                ;

        String companyId = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class)
                .getId();

        CompanyDto wallmartDto = CompanyDto
                .builder()
                .name("wallmart")
                .contactName("wallmartCEO")
                .build();


        AddressDto addressWallmartDto= new AddressDto();
        addressWallmartDto.setLine1("Address line 1");
        addressWallmartDto.setLine2("line 2");
        addressWallmartDto.setCity("New York");
        addressWallmartDto.setState("PR");
        addressWallmartDto.setZipcode(12343);

        wallmartDto.setAddress(addressWallmartDto);

        RequestBuilder updateRequest = RestDocumentationRequestBuilders.put("/company/{companyId}",companyId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wallmartDto));

        mockMvc.perform(updateRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").value("Company update Successfully."))
                .andExpect(jsonPath("id").exists())
                .andDo(document("update-company", pathParameters(
                        parameterWithName("companyId").description("Company Id")
                ),requestFields(
                        fieldWithPath("name").description("Name of the Company").optional(),
                        fieldWithPath("contactName").description("Name of the Contact person of the company").type("String").optional(),
                        fieldWithPath("contactTitle").description("Title of the Contact person of the company").type("String").optional(),
                        fieldWithPath("contactNumber").description("Contact No of the company PoC").type("Integer").optional(),
                        fieldWithPath("address.line1").description("Address line 1 of the Company").type("String").optional(),
                        fieldWithPath("address.line2").description("Address line 2 of the Company").type("String").optional(),
                        fieldWithPath("address.city").description("City of the Company location").type("String").optional(),
                        fieldWithPath("address.state").description("State of the Company location").type("String").optional(),
                        fieldWithPath("address.zipcode").description("Zip-Code of the Company location").type("Integer").optional()

                ),responseFields(
                        fieldWithPath("responseMessage").description("Response Message.").optional(),
                        fieldWithPath("id").description("Uniquely Identifier of the Company.").type("Long").optional()
                        )))
        ;

        RequestBuilder getCompanyByIdRequest = RestDocumentationRequestBuilders.get("/company/{companyId}","1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getCompanyByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("wallmart"))
                .andExpect(jsonPath("contactName").value("wallmartCEO"))
                .andExpect(jsonPath("contactNumber").value("1234567890"))
                .andExpect(jsonPath("contactTitle").value("Contact Title"))
                .andExpect(jsonPath("address.line1").value("Address line 1"))
                .andExpect(jsonPath("address.line2").value("line 2"))
                .andExpect(jsonPath("address.city").value("New York"))
                .andExpect(jsonPath("address.state").value("PR"))
                .andExpect(jsonPath("address.zipcode").value("12343"))
                .andDo(print())
                .andDo(document("get-company-by-id", pathParameters(
                        parameterWithName("companyId").description("Company Id")
                ),responseFields(
                        fieldWithPath("name").description("Name of the Company"),
                        fieldWithPath("id").description("Uniquely Identifier of the Company."),
                        fieldWithPath("contactName").description("Name of the Contact person of the company"),
                        fieldWithPath("contactTitle").description("Title of the Contact person of the company"),
                        fieldWithPath("contactNumber").description("Contact No of the company PoC"),
                        fieldWithPath("invoices").ignored().optional(),
                        fieldWithPath("address.line1").description("Address line 1 of the Company"),
                        fieldWithPath("address.line2").description("Address line 2 of the Company"),
                        fieldWithPath("address.city").description("City of the Company location"),
                        fieldWithPath("address.state").description("State of the Company location"),
                        fieldWithPath("address.zipcode").description("Zip-Code of the Company location")
                )))
        ;
    }

    @Test
    @DirtiesContext()
    public void listCompanies() throws Exception {
        RequestBuilder createCompany = post("/company")
                .content(objectMapper.writeValueAsString(CompanyDto.builder()
                        .name("Test Name")
                        .contactName("Contact Name")
                        .contactTitle("Contact Title")
                        .contactNumber(1234567890L)
                        .address(AddressDto.builder()
                                .line1("Address line 1")
                                .line2("line 2")
                                .city("City")
                                .state("XX")
                                .zipcode(12343)
                                .build())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result=  mockMvc.perform(createCompany)
                .andExpect(status().isCreated())
                .andReturn()
                ;

        String companyId = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class)
                .getId();

        CompanyDto wallmartDto = CompanyDto
                .builder()
                .name("wallmart")
                .contactName("wallmartCEO")
                .build();


        AddressDto addressWallmartDto= new AddressDto();
        addressWallmartDto.setLine1("Address line 1");
        addressWallmartDto.setLine2("line 2");
        addressWallmartDto.setCity("New York");
        addressWallmartDto.setState("PR");
        addressWallmartDto.setZipcode(12343);

        wallmartDto.setAddress(addressWallmartDto);

        RequestBuilder updateRequest = put(String.format("/company/%s",companyId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wallmartDto));

        mockMvc.perform(updateRequest)
                .andExpect(status().isCreated())
                .andDo(document("UpdateCompany"));


        //List Companies
        mockMvc.perform(get("/company/all")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value("wallmart"))
                .andExpect(jsonPath("$[0].contactName").value("wallmartCEO"))
                .andExpect(jsonPath("$[0].contactNumber").value("1234567890"))
                .andExpect(jsonPath("$[0].contactTitle").value("Contact Title"))
                .andExpect(jsonPath("$[0].address.line1").value("Address line 1"))
                .andExpect(jsonPath("$[0].address.line2").value("line 2"))
                .andExpect(jsonPath("$[0].address.city").value("New York"))
                .andExpect(jsonPath("$[0].address.state").value("PR"))
                .andExpect(jsonPath("$[0].address.zipcode").value("12343"))

                // Follow Up to andExpect
                .andDo(document("company-list", responseFields(
                        fieldWithPath("[]").description("An array of Company Details"),
                        fieldWithPath("[].id").description("Uniquely Identifier of the Company."),
                        fieldWithPath("[].name").description("Name of the Company."),
                        fieldWithPath("[].contactName").description("Contact Name of the Company."),
                        fieldWithPath("[].contactNumber").description("Contact Number of the Company."),
                        fieldWithPath("[].contactTitle").description("Contact Title of the Company."),
                        fieldWithPath("[].address").description("Address Object of Company").type("Address"),
                        fieldWithPath("[].address.line1").description("Address line 1"),
                        fieldWithPath("[].address.line2").description("line 2"),
                        fieldWithPath("[].address.city").description("New York"),
                        fieldWithPath("[].address.state").description("PR"),
                        fieldWithPath("[].address.zipcode").description("12343")
                )));

    }
    @Test
    public void getCompanyListSimpleView() throws Exception {
        RequestBuilder createCompany = post("/company")
                .content(objectMapper.writeValueAsString(CompanyDto.builder()
                        .name("Test Name")
                        .contactName("Contact Name")
                        .contactTitle("Contact Title")
                        .contactNumber(1234567890L)
                        .address(AddressDto.builder()
                                .line1("Address line 1")
                                .line2("line 2")
                                .city("City")
                                .state("TX")
                                .zipcode(12343)
                                .build())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result=  mockMvc.perform(createCompany)
                .andExpect(status().isCreated())
                .andReturn()
                ;



        RequestBuilder getCompanyListSimpleView=get("/company/all/simple");
        mockMvc.perform(getCompanyListSimpleView)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("Test Name"))
                .andExpect(jsonPath("$[0].city").value("City"))
                .andExpect(jsonPath("$[0].state").value("TX"))
                .andDo(document("company-list-simple", responseFields(
                        fieldWithPath("[]").description("An Array of Simple details of Company"),
                        fieldWithPath("[].companyName").description("Company Name"),
                        fieldWithPath("[].city").description("City"),
                        fieldWithPath("[].state").description("State")
                )))
        ;
    }
}