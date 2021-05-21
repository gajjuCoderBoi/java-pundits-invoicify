package com.cognizant.javapunditsinvoicify.service;

import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import com.cognizant.javapunditsinvoicify.exception.InvalidDataException;
import com.cognizant.javapunditsinvoicify.exception.MyEntityNotFoundException;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceItemMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceMapper;
import com.cognizant.javapunditsinvoicify.misc.FeeType;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceItemRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.util.DateFormatUtil;
import com.cognizant.javapunditsinvoicify.util.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.cognizant.javapunditsinvoicify.util.InvoicifyConstants.DESCENDING;
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

    @Autowired
    private HelperMethods helperMethods;

    public ResponseMessage addInvoiceItem(InvoiceItemDto invoiceItemDto, Long invoiceId) {
        InvoiceEntity savedInvoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (savedInvoice == null)
        {
            throw new MyEntityNotFoundException("Invalid Invoice Id. Not Found.");
        }

        InvoiceItemEntity invoiceItemEntity = invoiceItemMapper.invoiceItemDtoToEntity(invoiceItemDto);
        if(invoiceItemDto.getFeeType() == FeeType.FLAT ){
            invoiceItemEntity.setRate(null);invoiceItemEntity.setQuantity(null);
            if(invoiceItemDto.getAmount() == null)
                throw new InvalidDataException("FLAT Item Amount Cannot be empty.");
                }
        else {
            invoiceItemEntity.setAmount(null);
            if(invoiceItemDto.getQuantity() == null){
                throw new InvalidDataException("RATE Item Quantity Cannot be empty.") ;
            }
            if(invoiceItemDto.getRate() == null){
                throw new InvalidDataException("RATE Item Rate Cannot be empty.");
            }
        }
        invoiceItemEntity.setInvoiceEntity(savedInvoice);
        invoiceItemEntity = invoiceItemRepository.save(invoiceItemEntity);

        return ResponseMessage.builder()
                .responseMessage("InvoiceItem added Successfully.")
                .id(String.valueOf(invoiceItemEntity.getId()))
                .httpStatus(CREATED)
                .build();
    }

    @Transactional
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

        if(invoiceDto.getItems() != null)
        for (InvoiceItemDto itemDto:invoiceDto.getItems()) {
                addInvoiceItem(itemDto, invoiceEntity.getId());
            }

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
        }else{
            invoiceDto.setCreatedDate(null);
            invoiceDto.setModifiedDate(null);
        }
        double calculatedTotal = 0.0;
        if(invoiceEntity.getInvoiceItemEntityList()!=null) {
            calculatedTotal = invoiceEntity.getInvoiceItemEntityList().stream().mapToDouble(invoiceItemEntity -> {
                        if (invoiceItemEntity.getFeeType() == FeeType.FLAT)
                            return invoiceItemEntity.getAmount();
                        else return invoiceItemEntity.getRate() * invoiceItemEntity.getQuantity();
                    }
            ).sum();
            invoiceDto.setItems(
                    invoiceEntity.getInvoiceItemEntityList().stream().map(entity->{
                        {
                            InvoiceItemDto invoiceItemDto =  invoiceItemMapper.invoiceItemEntityToDto(entity);
                            if(entity.getFeeType() == FeeType.FLAT ){ invoiceItemDto.setRate(null);invoiceItemDto.setQuantity(null); }
                            else { invoiceItemDto.setAmount(null); }
                            return invoiceItemDto;
                        }
                    }).collect(Collectors.toList())
            );
        }

        invoiceDto.setTotal(calculatedTotal);
        return invoiceDto;
    }

    public ResponseMessage updateInvoice(InvoiceDto invoiceDto, Long invoiceId) {
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
            if (savedInvoiceEntity.getPaymentStatus().equals(PaymentStatus.UNPAID))
            {
                savedInvoiceEntity.setPaymentStatus(invoiceDto.getPaymentStatus());
                savedInvoiceEntity.setModifiedDate(ZonedDateTime.now());
                invoiceRepository.save(savedInvoiceEntity);
                responseMessage.setId(savedInvoiceEntity.getId().toString());
                responseMessage.setResponseMessage("Invoice updated successfully.");
                responseMessage.setHttpStatus(ACCEPTED);
            }
            else
            {
                responseMessage.setId(savedInvoiceEntity.getId().toString());
                responseMessage.setResponseMessage("Invoice NOT updated.");
                responseMessage.setHttpStatus(NOT_ACCEPTABLE);
            }
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
            if(existingInvoiceEntity.getPaymentStatus().equals(PaymentStatus.PAID)
            && existingInvoiceEntity.getCreatedDate().plusYears(1).isBefore(ZonedDateTime.now()))
            {
                invoiceRepository.delete(existingInvoiceEntity);
                responseMessage.setId(existingInvoiceEntity.getId().toString());
                responseMessage.setResponseMessage("Invoice deleted successfully.");
                responseMessage.setHttpStatus(ACCEPTED);
            }
            else
            {
                responseMessage.setId(existingInvoiceEntity.getId().toString());
                responseMessage.setResponseMessage("Unpaid/Recent Invoice cannot be deleted.");
                responseMessage.setHttpStatus(NOT_ACCEPTABLE);
            }
        }

        return responseMessage;
    }

    public List<InvoiceDto> getAllInvoices(Integer pageNo, Integer pageSize, String sortBy, String orderBy) {
        Pageable paging = PageRequest.of(
                pageNo,
                pageSize,
                orderBy.equals(DESCENDING) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending()
                );

        Page<InvoiceEntity> pagedResult = invoiceRepository.findAll(paging);

        return helperMethods.convertInvoiceEntityToDtoList(pagedResult.toList());
    }

    public List<InvoiceDto> getCompanyUnpaidInvoices(Long companyId) {
        CompanyEntity savedCompanyEntity=companyRepository.findById(companyId).orElse(null);
        List<InvoiceEntity> unpaidInvoiceEntity=invoiceRepository
                .findInvoiceEntitiesByCompanyEntityAndPaymentStatusEquals(savedCompanyEntity,PaymentStatus.UNPAID);

        return helperMethods.convertInvoiceEntityToDtoList(unpaidInvoiceEntity);
    }
}
