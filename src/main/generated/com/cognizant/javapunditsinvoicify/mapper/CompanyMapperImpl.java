package com.cognizant.javapunditsinvoicify.mapper;

import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto.CompanyDtoBuilder;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto.InvoiceDtoBuilder;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity.CompanyEntityBuilder;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-05-19T18:27:42-0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.10 (Oracle Corporation)"
)
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyEntity companyDtoToEntity(CompanyDto dto) {
        if ( dto == null ) {
            return null;
        }

        CompanyEntityBuilder companyEntity = CompanyEntity.builder();

        companyEntity.name( dto.getName() );
        companyEntity.contactName( dto.getContactName() );
        companyEntity.contactTitle( dto.getContactTitle() );
        companyEntity.contactNumber( dto.getContactNumber() );
        companyEntity.invoices( invoiceDtoListToInvoiceEntityList( dto.getInvoices() ) );

        return companyEntity.build();
    }

    @Override
    public CompanyDto companyEntityToDto(CompanyEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CompanyDtoBuilder companyDto = CompanyDto.builder();

        if ( entity.getCompanyId() != null ) {
            companyDto.id( String.valueOf( entity.getCompanyId() ) );
        }
        companyDto.name( entity.getName() );
        companyDto.contactName( entity.getContactName() );
        companyDto.contactTitle( entity.getContactTitle() );
        companyDto.contactNumber( entity.getContactNumber() );
        companyDto.invoices( invoiceEntityListToInvoiceDtoList( entity.getInvoices() ) );

        return companyDto.build();
    }

    protected InvoiceEntity invoiceDtoToInvoiceEntity(InvoiceDto invoiceDto) {
        if ( invoiceDto == null ) {
            return null;
        }

        InvoiceEntity invoiceEntity = new InvoiceEntity();

        invoiceEntity.setId( invoiceDto.getId() );
        if ( invoiceDto.getCreatedDate() != null ) {
            invoiceEntity.setCreatedDate( ZonedDateTime.parse( invoiceDto.getCreatedDate() ) );
        }
        if ( invoiceDto.getModifiedDate() != null ) {
            invoiceEntity.setModifiedDate( ZonedDateTime.parse( invoiceDto.getModifiedDate() ) );
        }
        invoiceEntity.setPaymentStatus( invoiceDto.getPaymentStatus() );

        return invoiceEntity;
    }

    protected List<InvoiceEntity> invoiceDtoListToInvoiceEntityList(List<InvoiceDto> list) {
        if ( list == null ) {
            return null;
        }

        List<InvoiceEntity> list1 = new ArrayList<InvoiceEntity>( list.size() );
        for ( InvoiceDto invoiceDto : list ) {
            list1.add( invoiceDtoToInvoiceEntity( invoiceDto ) );
        }

        return list1;
    }

    protected InvoiceDto invoiceEntityToInvoiceDto(InvoiceEntity invoiceEntity) {
        if ( invoiceEntity == null ) {
            return null;
        }

        InvoiceDtoBuilder invoiceDto = InvoiceDto.builder();

        invoiceDto.id( invoiceEntity.getId() );
        if ( invoiceEntity.getCreatedDate() != null ) {
            invoiceDto.createdDate( DateTimeFormatter.ISO_DATE_TIME.format( invoiceEntity.getCreatedDate() ) );
        }
        if ( invoiceEntity.getModifiedDate() != null ) {
            invoiceDto.modifiedDate( DateTimeFormatter.ISO_DATE_TIME.format( invoiceEntity.getModifiedDate() ) );
        }
        invoiceDto.paymentStatus( invoiceEntity.getPaymentStatus() );

        return invoiceDto.build();
    }

    protected List<InvoiceDto> invoiceEntityListToInvoiceDtoList(List<InvoiceEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<InvoiceDto> list1 = new ArrayList<InvoiceDto>( list.size() );
        for ( InvoiceEntity invoiceEntity : list ) {
            list1.add( invoiceEntityToInvoiceDto( invoiceEntity ) );
        }

        return list1;
    }
}
