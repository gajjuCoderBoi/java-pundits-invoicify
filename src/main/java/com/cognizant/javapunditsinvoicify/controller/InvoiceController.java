package com.cognizant.javapunditsinvoicify.controller;

import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.InvoiceService;
import com.cognizant.javapunditsinvoicify.util.InvoicifyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cognizant.javapunditsinvoicify.util.InvoicifyConstants.ASCENDING;
import static com.cognizant.javapunditsinvoicify.util.InvoicifyConstants.MAX_PAGE_SIZE;
import static java.lang.Math.max;
import static org.springframework.http.HttpStatus.OK;

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

    @GetMapping
    public ResponseEntity<?> getAllInvoices(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "createdDate") String sortBy,
            @RequestParam(name = "orderBy", defaultValue = ASCENDING) String orderBy
    ){
        List<InvoiceDto> invoices = invoiceService.getAllInvoices(pageNo, Math.min(pageSize, MAX_PAGE_SIZE), sortBy, orderBy);
        return new ResponseEntity<>(invoices, OK);
    }

    @GetMapping("/{companyId}/unpaid")
    public ResponseEntity<?> getCompanyUnpaidInvoices(
            @PathVariable(name="companyId") Long companyId
    ){
        return new ResponseEntity<>("Hello", OK);

    }

}
