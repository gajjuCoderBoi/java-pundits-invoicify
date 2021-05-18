package com.cognizant.javapunditsinvoicify.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BaseController {

    @GetMapping
    public Map<String, String> greetings(){
        Map<String, String> res = new HashMap<>();
        res.put("greetings", "Welcome to Java Pundits Invoicify App.");
        res.put("api_docs", "https://java-pundits-invoicify.herokuapp.com/docs/index.html");
        return res;
    }

}
