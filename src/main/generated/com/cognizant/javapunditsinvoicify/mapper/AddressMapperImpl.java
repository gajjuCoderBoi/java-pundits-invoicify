package com.cognizant.javapunditsinvoicify.mapper;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-05-17T17:46:55-0500",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.10 (Oracle Corporation)"
)
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressEntity addressDtoToEntity(AddressDto dto) {
        if ( dto == null ) {
            return null;
        }

        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setLine1( dto.getLine1() );
        addressEntity.setLine2( dto.getLine2() );
        addressEntity.setCity( dto.getCity() );
        addressEntity.setState( dto.getState() );
        addressEntity.setZip( dto.getZipcode() );

        return addressEntity;
    }

    @Override
    public AddressDto addressEntityToDto(AddressEntity entity) {
        if ( entity == null ) {
            return null;
        }

        AddressDto addressDto = new AddressDto();

        addressDto.setLine1( entity.getLine1() );
        addressDto.setLine2( entity.getLine2() );
        addressDto.setCity( entity.getCity() );
        addressDto.setState( entity.getState() );
        addressDto.setZipcode( entity.getZip() );

        return addressDto;
    }
}
