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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    public void createCompanyTest() throws Exception{

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
     * @author Raghavendra, Koustav
     * To validate the dupe entries are not allowed
     */

    @Test
    public void createDuplicateCompanyTest() throws Exception{

        CompanyDto CompanyDto = new CompanyDto();
        CompanyDto.setName("First Company");
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
    public void updateCompany_Success() {
        CompanyDto wallmart= CompanyDto
                .builder()
                .build();
        RequestBuilder updateRequest = put("/company/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


    }


}
