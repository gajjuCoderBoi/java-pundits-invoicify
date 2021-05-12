package com.cognizant.javapunditsinvoicify.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("invoice")
public class InvoiceController {

    @PostMapping
    public void addInvoiceItem(@RequestParam("invoice_id") Long invoiceId){
        return;
    }

}
