package com.cognizant.javapunditsinvoicify.mapper;


import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE, unmappedSourcePolicy = IGNORE)
public interface InvoiceItemMapper {

    @Mappings({
            @Mapping(source = "entity.description", target = "description"),
            @Mapping(source = "entity.quantity", target = "quantity"),
            @Mapping(source = "entity.amount", target = "amount"),
            @Mapping(source = "entity.feeType", target = "feeType"),
            @Mapping(source = "entity.rate", target = "rate")
    })
    InvoiceItemDto invoiceItemEntityToDto(InvoiceItemEntity entity);

    @Mappings({
            @Mapping(source = "dto.description", target = "description"),
            @Mapping(source = "dto.quantity", target = "quantity"),
            @Mapping(source = "dto.amount", target = "amount"),
            @Mapping(source = "dto.feeType", target = "feeType"),
            @Mapping(source = "dto.rate", target = "rate")
    })
    InvoiceItemEntity invoiceItemDtoToEntity(InvoiceItemDto dto);
    
}
