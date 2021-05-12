package com.cognizant.javapunditsinvoicify.mapper;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    @Mappings({
            @Mapping(source="dto.line1",target="line1"),
            @Mapping(source="dto.line2",target="line2"),
            @Mapping(source="dto.city",target="city"),
            @Mapping(source="dto.state",target="state"),
            @Mapping(source="dto.zipcode",target="zip")
    })
    AddressEntity addressDtoToEntity(AddressDto dto);

    @Mappings({
            @Mapping(source="entity.line1",target="line1"),
            @Mapping(source="entity.line2",target="line2"),
            @Mapping(source="entity.city",target="city"),
            @Mapping(source="entity.state",target="state"),
            @Mapping(source="entity.zip",target="zipcode")
    })
    AddressDto addressEntityToDto(AddressEntity entity);


}
