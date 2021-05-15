package com.cognizant.javapunditsinvoicify.mapper;

import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, unmappedSourcePolicy = IGNORE)
public interface InvoiceMapper {

    @Mappings({
            @Mapping(source = "entity.paymentStatus", target = "paymentStatus"),
            @Mapping(source = "entity.total", target = "total")
    })
    InvoiceDto invoiceEntityToDto(InvoiceEntity entity);

    @Mappings({
            @Mapping(source = "dto.paymentStatus", target = "paymentStatus"),
            @Mapping(source = "dto.total", target = "total")
    })
    InvoiceEntity invoiceDtoToEntity(InvoiceDto dto);
}
