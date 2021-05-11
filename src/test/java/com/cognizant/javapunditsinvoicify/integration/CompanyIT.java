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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@DirtiesContext
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

        CompanyDto CompanyDto = new CompanyDto();
        CompanyDto.setName("Name");
        CompanyDto.setContactName("Contact Name");
        CompanyDto.setContactTitle("Contact Title");
        CompanyDto.setContactNumber(123456789);
        CompanyDto.setInvoices("Invoices");

        AddressDto AddressDto = new AddressDto();
        AddressDto.setLine1("Address line 1");
        AddressDto.setLine2("line 2");
        AddressDto.setCity("City");
        AddressDto.setState("XX");
        AddressDto.setZipcode(12345);

        CompanyDto.setAddress(AddressDto);

        RequestBuilder rq = post("/company")
                .content(objectMapper.writeValueAsString(CompanyDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
                .andExpect(status().isCreated())
                .andDo(print())

        ;
    }

    /**
     * @author Raghavendra, Koustav
     * To validate the dupe entries are not allowed
     */

    @Test
    public void createDuplicateCompanyTest() throws Exception{

        CompanyDto CompanyDto = new CompanyDto();
        CompanyDto.setName("First Company");
        CompanyDto.setContactName("Contact Name");
        CompanyDto.setContactTitle("Contact Title");
        CompanyDto.setContactNumber(123456789);
        CompanyDto.setInvoices("Invoices");

        AddressDto AddressDto = new AddressDto();
        AddressDto.setLine1("Address line 1");
        AddressDto.setLine2("line 2");
        AddressDto.setCity("City");
        AddressDto.setState("XX");
        AddressDto.setZipcode(12345);

        CompanyDto.setAddress(AddressDto);

        RequestBuilder rq = post("/company")
                .content(objectMapper.writeValueAsString(CompanyDto))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("responseMessage").exists());

        ///Repushing the same response in order to validate the dupe logic


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
                        .contactNumber(123456789)
                        .invoices("Invoices")
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
                .getResponseMessage();

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
                .andExpect(status().isNoContent())
                .andDo(document("UpdateCompany"));

        RequestBuilder getCompanyByIdRequest = get(String.format("/company/%s",companyId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getCompanyByIdRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("wallmart"))
                .andExpect(jsonPath("contactName").value("wallmartCEO"))
                .andExpect(jsonPath("contactNumber").value("123456789"))
                .andExpect(jsonPath("contactTitle").value("Contact Title"))
                .andExpect(jsonPath("address.line1").value("Address line 1"))
                .andExpect(jsonPath("address.line2").value("line 2"))
                .andExpect(jsonPath("address.city").value("New York"))
                .andExpect(jsonPath("address.state").value("PR"))
                .andExpect(jsonPath("address.zipcode").value("12343"))
                .andDo(print());
    }
    @Test
    @DirtiesContext()
    public void listCompanies() throws Exception {
        RequestBuilder createCompany = post("/company")
                .content(objectMapper.writeValueAsString(CompanyDto.builder()
                        .name("Test Name")
                        .contactName("Contact Name")
                        .contactTitle("Contact Title")
                        .contactNumber(123456789)
                        .invoices("Invoices")
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
                .getResponseMessage();

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
                .andExpect(status().isNoContent())
                .andDo(document("UpdateCompany"));


        //List Companies
        mockMvc.perform(get("/company/all")
        ).andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("[0].name").value("wallmart"))
                .andExpect(jsonPath("[0].contactName").value("wallmartCEO"))
                .andExpect(jsonPath("[0].contactNumber").value("123456789"))
                .andExpect(jsonPath("[0].contactTitle").value("Contact Title"))
                .andExpect(jsonPath("[0].invoices").value("Invoices"))
                .andExpect(jsonPath("[0].address.line1").value("Address line 1"))
                .andExpect(jsonPath("[0].address.line2").value("line 2"))
                .andExpect(jsonPath("[0].address.city").value("New York"))
                .andExpect(jsonPath("[0].address.state").value("PR"))
                .andExpect(jsonPath("[0].address.zipcode").value("12343"))

                // Follow Up to andExpect
                .andDo(document("Companies", responseFields(
                        fieldWithPath("[0].name").description("wallmart"),
                        fieldWithPath("[0].contactName").description("wallmartCEO"),
                        fieldWithPath("[0].contactNumber").description("123456789"),
                        fieldWithPath("[0].invoices").description("Invoices"),
                        fieldWithPath("[0].contactTitle").description("Contact Title 3"),
                        fieldWithPath("[0].address.line1").description("Address line 1"),
                        fieldWithPath("[0].address.line2").description("line 2"),
                        fieldWithPath("[0].address.city").description("New York"),
                        fieldWithPath("[0].address.state").description("PR"),
                        fieldWithPath("[0].address.zipcode").description("12343")
                )));

    }

}
