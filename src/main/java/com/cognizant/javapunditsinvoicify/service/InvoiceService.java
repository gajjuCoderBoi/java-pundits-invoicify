package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceItemMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceMapper;
import com.cognizant.javapunditsinvoicify.misc.FeeType;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceItemRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.util.DateFormatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
                .id(String.valueOf(invoiceItemEntity.getId()))
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
        invoiceEntity.setCreatedDate(ZonedDateTime.now());
        invoiceEntity.setModifiedDate(ZonedDateTime.now());
        invoiceEntity = invoiceRepository.save(invoiceEntity);
        return ResponseMessage.builder()
                .id(invoiceEntity.getId().toString())
                .responseMessage("Invoice created.")
                .httpStatus(CREATED)
                .build();
    }

    public InvoiceDto getInvoiceById(Long invoiceId) {

        InvoiceEntity invoiceEntity=invoiceRepository.findById(invoiceId).orElse(null);

        InvoiceDto invoiceDto= invoiceMapper.invoiceEntityToDto(invoiceEntity);

        if(invoiceEntity.getCreatedDate() != null) {
            invoiceDto.setCreatedDate(DateFormatUtil.formatDate(invoiceEntity.getCreatedDate()));
            invoiceDto.setModifiedDate(DateFormatUtil.formatDate(invoiceEntity.getModifiedDate()));
        }
        Double calculatedTotal = 0.0;
        if(invoiceEntity.getInvoiceItemEntityList()!=null) {
            calculatedTotal = invoiceEntity.getInvoiceItemEntityList().stream().mapToDouble(invoiceItemEntity -> {
                System.out.println(invoiceItemEntity.getFeeType());
                        if (invoiceItemEntity.getFeeType() == FeeType.FLAT)
                            return invoiceItemEntity.getAmount();
                        else return invoiceItemEntity.getRate() * invoiceItemEntity.getQuantity();
                    }
            ).sum();
            invoiceDto.setItems(
                    invoiceEntity.getInvoiceItemEntityList().stream().map(entity->{
                        return invoiceItemMapper.invoiceItemEntityToDto(entity);
                    }).collect(Collectors.toList())
            );
        }

        invoiceDto.setTotal(calculatedTotal);
        return invoiceDto;
    }
}
