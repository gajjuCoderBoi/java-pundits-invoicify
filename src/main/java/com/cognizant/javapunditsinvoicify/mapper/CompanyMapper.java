package com.cognizant.javapunditsinvoicify.mapper;

import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CompanyMapper {
    @Mappings({
            @Mapping(source = "dto.name", target = "name"),
            @Mapping(source = "dto.contactName", target = "contactName"),
            @Mapping(source = "dto.contactTitle", target = "contactTitle"),
            @Mapping(source = "dto.contactNumber", target = "contactNumber")
    })
    CompanyEntity companyDtoToEntity(CompanyDto dto);

    @Mappings({
            @Mapping(source = "entity.name", target = "name"),
            @Mapping(source = "entity.contactName", target = "contactName"),
            @Mapping(source = "entity.contactTitle", target = "contactTitle"),
            @Mapping(source = "entity.contactNumber", target = "contactNumber")
    })
    CompanyDto companyEntityToDto(CompanyEntity entity);
}
