package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceItemMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceMapper;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceItemRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.*;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    @Qualifier("invoice-item-mapper")
    private InvoiceItemMapper invoiceItemMapper;

    @Autowired
    @Qualifier("invoice-mapper")
    private InvoiceMapper invoiceMapper;

    public ResponseMessage addInvoiceItem(InvoiceItemDto invoiceItemDto, Long invoiceId) {
        InvoiceEntity savedInvoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (savedInvoice == null)
        {
            return ResponseMessage.builder()
                    .responseMessage("Invalid Invoice Id. Not Found.")
                    .httpStatus(NOT_FOUND)
                    .build();
        }

        InvoiceItemEntity invoiceItemEntity = invoiceItemMapper.invoiceItemDtoToEntity(invoiceItemDto);
        invoiceItemEntity.setInvoiceEntity(savedInvoice);
        invoiceItemEntity = invoiceItemRepository.save(invoiceItemEntity);

        return ResponseMessage.builder()
                .responseMessage("InvoiceItem added Successfully.")
                .httpStatus(CREATED)
                .build();
    }

    public ResponseMessage addInvoice(InvoiceDto invoiceDto, Long companyId) {

        CompanyEntity savedCompanyEntity = companyRepository.findById(companyId).orElse(null);

        if(savedCompanyEntity == null)
        {
            return ResponseMessage.builder()
                    .responseMessage("No Company found.")
                    .httpStatus(NOT_FOUND)
                    .build();
        }

        InvoiceEntity invoiceEntity = invoiceMapper.invoiceDtoToEntity(invoiceDto);
        invoiceEntity.setPaymentStatus(invoiceDto.getPaymentStatus());
        invoiceEntity.setCompanyEntity(savedCompanyEntity);
        invoiceEntity = invoiceRepository.save(invoiceEntity);

        return ResponseMessage.builder()
                .id(invoiceEntity.getId().toString())
                .responseMessage("Invoice created.")
                .httpStatus(CREATED)
                .build();
    }

    public ResponseMessage updateInvoice(InvoiceDto invoiceDto, Long invoiceId)
    {
        ResponseMessage responseMessage = new ResponseMessage();
        InvoiceEntity savedInvoiceEntity;

        savedInvoiceEntity = invoiceRepository.findById(invoiceId).orElse(null);
        if (savedInvoiceEntity == null)
        {
            responseMessage.setId("0");
            responseMessage.setResponseMessage("Invoice does not exist.");
            responseMessage.setHttpStatus(NOT_FOUND);
        }
        else {

            if (invoiceDto.getPaymentStatus() != null) {
                savedInvoiceEntity.setPaymentStatus(invoiceDto.getPaymentStatus());
            }
            invoiceRepository.save(savedInvoiceEntity);
            responseMessage.setId(savedInvoiceEntity.getId().toString());
            responseMessage.setResponseMessage("Invoice updated successfully.");
            responseMessage.setHttpStatus(ACCEPTED);
        }

        return responseMessage;
    }

    public ResponseMessage deleteInvoice(Long invoiceId) {
        ResponseMessage responseMessage = new ResponseMessage();
        InvoiceEntity existingInvoiceEntity;

        existingInvoiceEntity = invoiceRepository.findById(invoiceId).orElse(null);
        if (existingInvoiceEntity == null)
        {
            responseMessage.setId("0");
            responseMessage.setResponseMessage("Invoice does not exist.");
            responseMessage.setHttpStatus(NOT_FOUND);
        }
        else {
            invoiceRepository.delete(existingInvoiceEntity);
            responseMessage.setId(existingInvoiceEntity.getId().toString());
            responseMessage.setResponseMessage("Invoice deleted successfully.");
            responseMessage.setHttpStatus(ACCEPTED);
        }

        return responseMessage;
    }
}
