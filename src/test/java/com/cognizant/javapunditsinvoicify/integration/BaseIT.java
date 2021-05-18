package com.cognizant.javapunditsinvoicify.integration;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("qa")
public class BaseIT {
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGreetings() throws Exception {
        RequestBuilder greetingsGet = get("/");

        mockMvc.perform(greetingsGet)
                .andExpect(status().isOk())
                .andExpect(jsonPath("api_docs").value("https://java-pundits-invoicify.herokuapp.com/docs/index.html"))
                .andExpect(jsonPath("greetings").value("Welcome to Java Pundits Invoicify App."));
    }



}