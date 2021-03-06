package com.cognizant.javapunditsinvoicify.unit.controller;

import com.cognizant.javapunditsinvoicify.controller.CompanyController;
import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.CompanyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CompanyController.class)
@ActiveProfiles("qa")
public class CompanyControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CompanyService companyService;
    
    private CompanyDto companyDto;
    private AddressDto addressDto;
    
    @BeforeEach
    void initMockData(){
        companyDto = new CompanyDto();
        companyDto.setName("Name");
        companyDto.setContactName("Contact Name");
        companyDto.setContactTitle("Contact Title");
        companyDto.setContactNumber(1234567890L);

        addressDto = new AddressDto();
        addressDto.setLine1("Address Line 1");
        addressDto.setLine2("Address Line 2");
        addressDto.setCity("City");
        addressDto.setState("XX");
        addressDto.setZipcode(12345);

        companyDto.setAddress(addressDto);
    }

    @Test
    public void updateCompanyTest() throws Exception {
        String companyId = "1";
        CompanyDto wallmartDto = CompanyDto
                .builder()
                .name("wallmart")
                .contactName("wallmartCEO")
                .address(AddressDto
                        .builder()
                        .city("New York")
                        .state("PR")
                        .build())
                .build();
        RequestBuilder updateRequest = put(String.format("/company/%s",companyId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wallmartDto));

        when(companyService.update(any(), anyLong())).thenReturn(ResponseMessage.builder()
                .responseMessage("Company update Successfully.")
                .httpStatus(HttpStatus.CREATED)
                .id(String.valueOf(1L))
                .build());

        mockMvc.perform(updateRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("responseMessage").value("Company update Successfully."))
                .andExpect(jsonPath("id").exists());
    }

    @Test
    public void getCompanyByIdTest() throws Exception {
        String companyId = "1";
        RequestBuilder getCompanyByIdGetRequest =  get(String.format("/company/%s",companyId))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        when(companyService.getCompanyById(anyLong())).thenReturn(companyDto);

        mockMvc.perform(getCompanyByIdGetRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Name"))
                .andExpect(jsonPath("contactName").value("Contact Name"))
                .andExpect(jsonPath("contactTitle").value("Contact Title"))
                .andExpect(jsonPath("contactNumber").value("1234567890"))
                .andExpect(jsonPath("address.line1").value("Address Line 1"))
                .andExpect(jsonPath("address.line2").value("Address Line 2"))
                .andExpect(jsonPath("address.city").value("City"))
                .andExpect(jsonPath("address.state").value("XX"))
                .andExpect(jsonPath("address.zipcode").value("12345"));
    }

    @Test
    public void addCompanyTest() throws Exception {
        String companyId = "1";
        RequestBuilder getCompanyByIdGetRequest =  post("/company")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(companyDto));

        ResponseMessage expectedResponse = new ResponseMessage();
        expectedResponse.setResponseMessage(companyId);
        expectedResponse.setHttpStatus(HttpStatus.OK);
        when(companyService.addCompany(companyDto)).thenReturn(expectedResponse);

        mockMvc.perform(getCompanyByIdGetRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("responseMessage").value(companyId));
    }

}
