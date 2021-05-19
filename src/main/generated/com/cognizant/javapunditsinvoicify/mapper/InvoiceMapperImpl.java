package com.cognizant.javapunditsinvoicify.mapper;

import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-05-17T17:46:55-0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.10 (Oracle Corporation)"
)
public class InvoiceMapperImpl implements InvoiceMapper {

    @Override
    public InvoiceDto invoiceEntityToDto(InvoiceEntity entity) {
        if ( entity == null ) {
            return null;
        }

        InvoiceDto invoiceDto = new InvoiceDto();

        invoiceDto.setPaymentStatus( entity.getPaymentStatus() );
        invoiceDto.setId( entity.getId() );
        if ( entity.getCreatedDate() != null ) {
            invoiceDto.setCreatedDate( DateTimeFormatter.ISO_DATE_TIME.format( entity.getCreatedDate() ) );
        }
        if ( entity.getModifiedDate() != null ) {
            invoiceDto.setModifiedDate( DateTimeFormatter.ISO_DATE_TIME.format( entity.getModifiedDate() ) );
        }

        return invoiceDto;
    }

    @Override
    public InvoiceEntity invoiceDtoToEntity(InvoiceDto dto) {
        if ( dto == null ) {
            return null;
        }

        InvoiceEntity invoiceEntity = new InvoiceEntity();

        invoiceEntity.setPaymentStatus( dto.getPaymentStatus() );
        invoiceEntity.setId( dto.getId() );
        if ( dto.getCreatedDate() != null ) {
            invoiceEntity.setCreatedDate( ZonedDateTime.parse( dto.getCreatedDate() ) );
        }
        if ( dto.getModifiedDate() != null ) {
            invoiceEntity.setModifiedDate( ZonedDateTime.parse( dto.getModifiedDate() ) );
        }

        return invoiceEntity;
    }
}
