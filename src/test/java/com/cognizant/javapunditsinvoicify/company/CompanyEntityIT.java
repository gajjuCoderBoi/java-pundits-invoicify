package com.cognizant.javapunditsinvoicify.company;

import com.cognizant.javapunditsinvoicify.dto.AddressDTO;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.dto.CompanyDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CompanyEntityIT {

    @InjectMocks
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createCompanyTest() throws Exception{

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Name");
        companyDTO.setContactName("Contact Name");
        companyDTO.setContactTitle("Contact Title");
        companyDTO.setContactNumber("Contact Number");
        companyDTO.setInvoices("Invoices");

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setLine1("Address line 1");
        addressDTO.setLine2("line 2");
        addressDTO.setCity("City");
        addressDTO.setState("XX");
        addressDTO.setZip(12345);

        companyDTO.setAddressDTO(addressDTO);

        RequestBuilder rq = post("/invoicify/companies")
                .content(objectMapper.writeValueAsString(companyDTO))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(rq)
                .andExpect(status().isCreated())
                .andDo(print())
        ;
    }

}
