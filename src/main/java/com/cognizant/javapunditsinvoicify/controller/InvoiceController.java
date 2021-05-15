package com.cognizant.javapunditsinvoicify.controller;

import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/item")
    public ResponseEntity<?> addInvoiceItem(@RequestBody InvoiceItemDto invoiceItemDto,
                                            @RequestParam("invoice_id") Long invoiceId)
    {
        ResponseMessage response = invoiceService.addInvoiceItem(invoiceItemDto, invoiceId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @PostMapping("/{companyId}")
    public ResponseEntity<?> addInvoice(@RequestBody InvoiceDto invoiceDto,
                                        @PathVariable(name="companyId") Long companyId)
    {
        ResponseMessage response = invoiceService.addInvoice(invoiceDto, companyId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @GetMapping("/{invoiceId}")
    public InvoiceDto getInvoiceById(@PathVariable (name= "invoiceId") Long invoiceId)
    {
        InvoiceDto invoiceDto=invoiceService.getInvoiceById(invoiceId);
        return invoiceDto;
    }
    @PutMapping("/{invoiceId}")
    public ResponseEntity<?> updateInvoice(@RequestBody InvoiceDto invoiceDto,
                                        @PathVariable(name="invoiceId") Long invoiceId)
    {
        ResponseMessage response = invoiceService.updateInvoice(invoiceDto, invoiceId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<?> deleteInvoice(@PathVariable(name="invoiceId") Long invoiceId)
    {
        ResponseMessage response = invoiceService.deleteInvoice(invoiceId);
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

}
