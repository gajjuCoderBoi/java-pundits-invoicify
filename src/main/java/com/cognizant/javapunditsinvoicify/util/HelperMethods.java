package com.cognizant.javapunditsinvoicify.util;

import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceItemMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceMapper;
import com.cognizant.javapunditsinvoicify.misc.FeeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class HelperMethods {

    @Autowired
    @Qualifier("invoice-mapper")
    private InvoiceMapper invoiceMapper;

    @Autowired
    @Qualifier("invoice-item-mapper")
    private InvoiceItemMapper invoiceItemMapper;

    public List<InvoiceDto> convertInvoiceEntityToDtoList(List<InvoiceEntity> invoiceEntityList){
        return invoiceEntityList.stream().map(invoiceEntity -> {
            InvoiceDto invoiceDto= invoiceMapper.invoiceEntityToDto(invoiceEntity);
            invoiceDto.setCompany(CompanyDto.builder()
                    .name(invoiceEntity.getCompanyEntity().getName())
                    .id(String.valueOf(invoiceEntity.getCompanyEntity().getCompanyId()))
                    .build());
            if(invoiceEntity.getCreatedDate() != null) {
                invoiceDto.setCreatedDate(DateFormatUtil.formatDate(invoiceEntity.getCreatedDate()));
                invoiceDto.setModifiedDate(DateFormatUtil.formatDate(invoiceEntity.getModifiedDate()));
            }
            Double calculatedTotal = 0.0;
            if(invoiceEntity.getInvoiceItemEntityList()!=null) {
                calculatedTotal = invoiceEntity.getInvoiceItemEntityList().stream().mapToDouble(invoiceItemEntity -> {
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
        }).collect(Collectors.toList());
    }

    public static boolean isNotEmpty(String value){
        return StringUtils.isNotEmpty(value) && StringUtils.isNotBlank(value);
    }
}
