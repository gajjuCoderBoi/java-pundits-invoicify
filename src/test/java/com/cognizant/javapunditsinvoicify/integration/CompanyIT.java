package com.cognizant.javapunditsinvoicify.integration;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
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
        CompanyDto.setContactTile("Contact Title");
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
     * @author Mohammad Javed, Jose Antonio
     */
    @Test
    @DirtiesContext()
    public void updateCompany_Success() throws Exception {
        RequestBuilder createCompany = post("/company")
                .content(objectMapper.writeValueAsString(CompanyDto.builder()
                        .name("Name")
                        .contactName("Contact Name")
                        .contactTile("Contact Title")
                        .contactNumber(123456789)
                        .invoices("Invoices")
                        .address(AddressDto.builder()
                                .line1("Address line 1")
                                .line2("line 2")
                                .city("City")
                                .state("XX")
                                .build())
                        .build()))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(createCompany)
                .andExpect(status().isCreated())
                .andDo(print())
        ;

        CompanyDto wallmartDto = CompanyDto
                .builder()
                .name("wallmart")
                .contactName("wallmartCEO")
                .build();
        RequestBuilder updateRequest = put("/company/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wallmartDto));

        mockMvc.perform(updateRequest)
                .andExpect(status().isNoContent())
                .andDo(print());

    }


}
