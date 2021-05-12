package com.cognizant.javapunditsinvoicify;

import com.cognizant.javapunditsinvoicify.mapper.AddressMapper;
import com.cognizant.javapunditsinvoicify.mapper.CompanyMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceItemMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JavaPunditsInvoicifyApplication {
    @Bean(name = "address-mapper")
    public AddressMapper addressMapperBean() {
        return Mappers.getMapper(AddressMapper.class);

    }

    @Bean(name = "company-mapper")
    public CompanyMapper companyMapperBean() {
        return Mappers.getMapper(CompanyMapper.class);
    }

    @Bean(name = "invoice-item-mapper")
    public InvoiceItemMapper invoiceItemMapperBean(){
        return Mappers.getMapper(InvoiceItemMapper.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(JavaPunditsInvoicifyApplication.class, args);
    }

}
