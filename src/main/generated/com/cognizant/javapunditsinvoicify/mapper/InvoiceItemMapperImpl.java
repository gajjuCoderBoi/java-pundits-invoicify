package com.cognizant.javapunditsinvoicify.mapper;

import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-05-17T17:46:56-0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.10 (Oracle Corporation)"
)
public class InvoiceItemMapperImpl implements InvoiceItemMapper {

    @Override
    public InvoiceItemDto invoiceItemEntityToDto(InvoiceItemEntity entity) {
        if ( entity == null ) {
            return null;
        }

        InvoiceItemDto invoiceItemDto = new InvoiceItemDto();

        invoiceItemDto.setDescription( entity.getDescription() );
        invoiceItemDto.setQuantity( entity.getQuantity() );
        invoiceItemDto.setAmount( entity.getAmount() );
        invoiceItemDto.setFeeType( entity.getFeeType() );
        invoiceItemDto.setRate( entity.getRate() );

        return invoiceItemDto;
    }

    @Override
    public InvoiceItemEntity invoiceItemDtoToEntity(InvoiceItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        InvoiceItemEntity invoiceItemEntity = new InvoiceItemEntity();

        invoiceItemEntity.setDescription( dto.getDescription() );
        invoiceItemEntity.setQuantity( dto.getQuantity() );
        invoiceItemEntity.setAmount( dto.getAmount() );
        invoiceItemEntity.setFeeType( dto.getFeeType() );
        invoiceItemEntity.setRate( dto.getRate() );

        return invoiceItemEntity;
    }
}
