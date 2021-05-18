package com.cognizant.javapunditsinvoicify.unit.service;

import com.cognizant.javapunditsinvoicify.dto.AddressDto;
import com.cognizant.javapunditsinvoicify.dto.CompanyDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceDto;
import com.cognizant.javapunditsinvoicify.dto.InvoiceItemDto;
import com.cognizant.javapunditsinvoicify.entity.AddressEntity;
import com.cognizant.javapunditsinvoicify.entity.CompanyEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceEntity;
import com.cognizant.javapunditsinvoicify.entity.InvoiceItemEntity;
import com.cognizant.javapunditsinvoicify.mapper.AddressMapper;
import com.cognizant.javapunditsinvoicify.mapper.CompanyMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceItemMapper;
import com.cognizant.javapunditsinvoicify.mapper.InvoiceMapper;
import com.cognizant.javapunditsinvoicify.misc.FeeType;
import com.cognizant.javapunditsinvoicify.misc.PaymentStatus;
import com.cognizant.javapunditsinvoicify.repository.CompanyRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceItemRepository;
import com.cognizant.javapunditsinvoicify.repository.InvoiceRepository;
import com.cognizant.javapunditsinvoicify.response.ResponseMessage;
import com.cognizant.javapunditsinvoicify.service.CompanyService;
import com.cognizant.javapunditsinvoicify.service.InvoiceService;
import com.cognizant.javapunditsinvoicify.util.DateFormatUtil;
import com.cognizant.javapunditsinvoicify.util.HelperMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.cognizant.javapunditsinvoicify.misc.FeeType.FLAT;
import static com.cognizant.javapunditsinvoicify.misc.FeeType.RATE;
import static com.cognizant.javapunditsinvoicify.misc.PaymentStatus.PAID;
import static com.cognizant.javapunditsinvoicify.misc.PaymentStatus.UNPAID;
import static com.cognizant.javapunditsinvoicify.util.InvoicifyConstants.ASCENDING;
import static com.cognizant.javapunditsinvoicify.util.InvoicifyConstants.DESCENDING;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("qa")
public class InvoiceServiceUnitTest {

    @InjectMocks
    private InvoiceService invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private InvoiceItemRepository invoiceItemRepository;

    @Mock
    private InvoiceItemMapper invoiceItemMapper;

    private InvoiceDto invoiceDto;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private HelperMethods helperMethods;


    private CompanyEntity mockCompanyEntity;
    private InvoiceEntity mockInvoiceEntity;
    private CompanyDto mockCompanyDto;
    private AddressDto addressDto;

    @BeforeEach
    void initMockData(){
        invoiceDto = new InvoiceDto();
        invoiceDto.setPaymentStatus(PaymentStatus.UNPAID);

        //Company Mock data
        mockCompanyEntity = new CompanyEntity();
        mockCompanyEntity.setName("Name");
        mockCompanyEntity.setContactName("Contact Name");
        mockCompanyEntity.setContactTitle("Contact Title");
        mockCompanyEntity.setContactNumber(123456789);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setLine1("Address Line 1");
        addressEntity.setLine2("Address Line 2");
        addressEntity.setCity("City");
        addressEntity.setState("XX");
        addressEntity.setZip(12345);

        mockCompanyEntity.setAddressEntity(addressEntity);

        mockCompanyDto = new CompanyDto();
        mockCompanyDto.setName("Name");
        mockCompanyDto.setContactName("Contact Name");
        mockCompanyDto.setContactTitle("Contact Title");
        mockCompanyDto.setContactNumber(123456789);

        addressDto = new AddressDto();
        addressDto.setLine1("Address Line 1");
        addressDto.setLine2("Address Line 2");
        addressDto.setCity("City");
        addressDto.setState("XX");
        addressDto.setZipcode(12345);

        mockCompanyDto.setAddress(addressDto);

        // Mock Invoice Entity object
        mockInvoiceEntity = new InvoiceEntity();
        mockInvoiceEntity.setId(1L);
        mockInvoiceEntity.setPaymentStatus(PaymentStatus.UNPAID);


    }

    @Test
    public void addInvoiceItem_Failed(){
        when(invoiceRepository.findById(anyLong())).thenReturn(java.util.Optional.of(new InvoiceEntity()));
        when(invoiceItemMapper.invoiceItemDtoToEntity(any(InvoiceItemDto.class))).thenReturn(new InvoiceItemEntity());
        when(invoiceItemRepository.save(any(InvoiceItemEntity.class))).thenReturn(new InvoiceItemEntity());

        ResponseMessage actualResponse = invoiceService.addInvoiceItem(InvoiceItemDto.builder()
                .description("Test Item")
                .build(), 1L);

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getResponseMessage(), "InvoiceItem added Successfully.");
        assertEquals(actualResponse.getHttpStatus(), CREATED);

    }

    @Test
    public void addInvoiceItem_Success(){
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseMessage actualResponse = invoiceService.addInvoiceItem(InvoiceItemDto.builder()
                .description("Test Item")
                .build(), 1L);

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getResponseMessage(), "Invalid Invoice Id. Not Found.");
        assertEquals(actualResponse.getHttpStatus(), NOT_FOUND);

    }

    @Test
    public void addInvoice_CompanyNotFound_Failed(){
        when(companyRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseMessage response = invoiceService.addInvoice(new InvoiceDto(), 1L);

        assertNotNull(response);
        assertEquals("No Company found." ,response.getResponseMessage());
        assertEquals(NOT_FOUND, response.getHttpStatus());

    }

    @Test
    public void updateInvoice_InvoiceNotFound_Failed(){
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseMessage actualResponse = invoiceService.updateInvoice(new InvoiceDto(), 1L);

        assertNotNull(actualResponse);
        assertEquals("Invoice does not exist.", actualResponse.getResponseMessage());
        assertEquals(NOT_FOUND, actualResponse.getHttpStatus());
    }

    @Test
    public void deleteInvoice_InvoiceNotFound_Failed(){
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseMessage actualResponse = invoiceService.deleteInvoice(1L);

        assertNotNull(actualResponse);
        assertEquals("Invoice does not exist.", actualResponse.getResponseMessage());
        assertEquals(NOT_FOUND, actualResponse.getHttpStatus());
    }

    @Test
    public void createInvoices() {
        when(invoiceMapper.invoiceDtoToEntity(any())).thenReturn(new InvoiceEntity());
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(new CompanyEntity()));
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId(1l);
        when(invoiceRepository.save(any(InvoiceEntity.class))).thenReturn(invoiceEntity);
        ResponseMessage actualResponse = invoiceService.addInvoice(invoiceDto, 1l);

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getId(), "1");
        assertEquals(actualResponse.getResponseMessage(), "Invoice created.");
        assertEquals(actualResponse.getHttpStatus(), CREATED);
    }

    @Test
    public void getInvoiceIdById(){
        ZonedDateTime _new, _modified;
        _new = _modified = ZonedDateTime.parse("2021-05-15T19:47:08.528563200-04:00[America/New_York]");
        mockInvoiceEntity = new InvoiceEntity();
        mockInvoiceEntity.setCompanyEntity(mockCompanyEntity);
        mockInvoiceEntity.setModifiedDate(_new);
        mockInvoiceEntity.setCreatedDate(_modified);
        mockInvoiceEntity.setPaymentStatus(PAID);

        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(mockInvoiceEntity));
        when(invoiceMapper.invoiceEntityToDto(any(InvoiceEntity.class))).thenReturn(new InvoiceDto());

        InvoiceDto actual = invoiceService.getInvoiceById(1L);

        assertNotNull(actual);
        assertEquals( "May 15, 2021 19:47 PM - Eastern Daylight Time", actual.getCreatedDate());
        assertEquals("May 15, 2021 19:47 PM - Eastern Daylight Time", actual.getModifiedDate());

    }

    @Test
    public void getInvoiceIdById_NoDate(){
        mockInvoiceEntity = new InvoiceEntity();
        mockInvoiceEntity.setCompanyEntity(mockCompanyEntity);
        mockInvoiceEntity.setModifiedDate(null);
        mockInvoiceEntity.setCreatedDate(null);
        mockInvoiceEntity.setPaymentStatus(PAID);

        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.of(mockInvoiceEntity));
        when(invoiceMapper.invoiceEntityToDto(any(InvoiceEntity.class))).thenReturn(new InvoiceDto());

        InvoiceDto actual = invoiceService.getInvoiceById(1L);

        assertNotNull(actual);
        assertNull(actual.getCreatedDate());
        assertNull(actual.getModifiedDate());
    }

    @Test
    public void getAllInvoices(){

        Pageable mockPaging = PageRequest.of(0, 10, Sort.by("createdDate").ascending());



        long count = 0;
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setCompanyId(++count);
        companyEntity.setName(randomAlphanumeric(10));
        companyEntity.setContactName("Contact Name");
        companyEntity.setContactTitle("Contact Title");
        companyEntity.setContactNumber(123456789);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setLine1("Address line 1");
        addressEntity.setLine2("Line 2");
        addressEntity.setCity("City");
        addressEntity.setState("XX");
        addressEntity.setZip(12345);
        companyEntity.setAddressEntity(addressEntity);
        companyEntity.setInvoices(new ArrayList<>());

        for(int j=1;j<=10;j++){
            InvoiceEntity invoiceEntity = new InvoiceEntity();
            invoiceEntity.setId(++count);
            invoiceEntity.setPaymentStatus(UNPAID);
            invoiceEntity.setCompanyEntity(companyEntity);
            invoiceEntity.setCreatedDate(ZonedDateTime.now());
            invoiceEntity.setModifiedDate(ZonedDateTime.now());
            invoiceEntity.setInvoiceItemEntityList(new ArrayList<>());
            for(int k=0;k<=2;k++){
                InvoiceItemEntity invoiceItemEntity = new InvoiceItemEntity();
                invoiceItemEntity.setId(++count);
                invoiceItemEntity.setInvoiceEntity(invoiceEntity);
                if(new Random().nextInt(100)%2 == 0){
                    invoiceItemEntity.setDescription(randomAlphanumeric(10)+"-FLAT");
                    invoiceItemEntity.setFeeType(FLAT);
                    invoiceItemEntity.setAmount(getRandomDouble(1, 100));
                }else{
                    invoiceItemEntity.setDescription(randomAlphanumeric(10)+"-RATE");
                    invoiceItemEntity.setFeeType(RATE);
                    invoiceItemEntity.setRate(getRandomDouble(1, 100));
                    invoiceItemEntity.setQuantity(getRandomInt(1, 10));
                }
                invoiceEntity.getInvoiceItemEntityList().add(invoiceItemEntity);
            }
            companyEntity.getInvoices().add(invoiceEntity);
        }
        InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class);
        InvoiceItemMapper invoiceItemMapper = Mappers.getMapper(InvoiceItemMapper.class);
        List<InvoiceDto> invoiceDtoList = companyEntity.getInvoices().stream().map(invoiceEntity -> {
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
        when(helperMethods.convertInvoiceEntityToDtoList(any())).thenReturn(invoiceDtoList);

        Page<InvoiceEntity> pageResult = new PageImpl<>(companyEntity.getInvoices());
        when(invoiceRepository.findAll(mockPaging)).thenReturn(pageResult);
        List<InvoiceDto> actual = invoiceService.getAllInvoices(0, 10, "createdDate", ASCENDING);

        assertNotNull(actual);
        assertEquals(10, actual.size());
        for(int i=0;i<companyEntity.getInvoices().size();i++){
            assertEquals(companyEntity.getInvoices().get(i).getPaymentStatus(), actual.get(i).getPaymentStatus());
        }

        mockPaging = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        when(helperMethods.convertInvoiceEntityToDtoList(any())).thenReturn(invoiceDtoList);
        when(invoiceRepository.findAll(mockPaging)).thenReturn(pageResult);
        actual = invoiceService.getAllInvoices(0, 10, "createdDate", DESCENDING);

        assertNotNull(actual);
        assertEquals(10, actual.size());
        for(int i=0;i<companyEntity.getInvoices().size();i++){
            assertEquals(companyEntity.getInvoices().get(i).getPaymentStatus(), actual.get(i).getPaymentStatus());
        }


    }

    @Test
    public void getCompanyUnpaidInvoices() {

        long count = 0;
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setCompanyId(++count);
        companyEntity.setName(randomAlphanumeric(10));
        companyEntity.setContactName("Contact Name");
        companyEntity.setContactTitle("Contact Title");
        companyEntity.setContactNumber(123456789);

        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setLine1("Address line 1");
        addressEntity.setLine2("Line 2");
        addressEntity.setCity("City");
        addressEntity.setState("XX");
        addressEntity.setZip(12345);
        companyEntity.setAddressEntity(addressEntity);
        companyEntity.setInvoices(new ArrayList<>());

        for(int j=1;j<=20;j++){
            InvoiceEntity invoiceEntity = new InvoiceEntity();
            invoiceEntity.setId(++count);
            invoiceEntity.setPaymentStatus(UNPAID);
            invoiceEntity.setCompanyEntity(companyEntity);
            invoiceEntity.setCreatedDate(ZonedDateTime.now());
            invoiceEntity.setModifiedDate(ZonedDateTime.now());
            invoiceEntity.setInvoiceItemEntityList(new ArrayList<>());
            for(int k=0;k<=2;k++){
                InvoiceItemEntity invoiceItemEntity = new InvoiceItemEntity();
                invoiceItemEntity.setId(++count);
                invoiceItemEntity.setInvoiceEntity(invoiceEntity);
                if(new Random().nextInt(100)%2 == 0){
                    invoiceItemEntity.setDescription(randomAlphanumeric(10)+"-FLAT");
                    invoiceItemEntity.setFeeType(FLAT);
                    invoiceItemEntity.setAmount(getRandomDouble(1, 100));
                }else{
                    invoiceItemEntity.setDescription(randomAlphanumeric(10)+"-RATE");
                    invoiceItemEntity.setFeeType(RATE);
                    invoiceItemEntity.setRate(getRandomDouble(1, 100));
                    invoiceItemEntity.setQuantity(getRandomInt(1, 10));
                }
                invoiceEntity.getInvoiceItemEntityList().add(invoiceItemEntity);
            }
            companyEntity.getInvoices().add(invoiceEntity);
        }

        when(companyRepository.findById(anyLong())).thenReturn(Optional.ofNullable(companyEntity));
        List<InvoiceEntity> mockUnpaidInvoiceEntities = new ArrayList<>();
        when(invoiceRepository.findInvoiceEntitiesByCompanyEntityAndPaymentStatusEquals(any(CompanyEntity.class), any(PaymentStatus.class))).thenReturn(companyEntity.getInvoices());
        InvoiceMapper invoiceMapper = Mappers.getMapper(InvoiceMapper.class);
        InvoiceItemMapper invoiceItemMapper = Mappers.getMapper(InvoiceItemMapper.class);
        List<InvoiceDto> invoiceDtoList = companyEntity.getInvoices().stream().map(invoiceEntity -> {
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
        when(helperMethods.convertInvoiceEntityToDtoList(anyList())).thenReturn(invoiceDtoList);
        List<InvoiceDto> actual = invoiceService.getCompanyUnpaidInvoices(companyEntity.getCompanyId());

        assertNotNull(actual);
        assertEquals(20, actual.size());
        for(int i=0;i<companyEntity.getInvoices().size();i++){
            assertEquals(companyEntity.getInvoices().get(i).getPaymentStatus(), actual.get(i).getPaymentStatus());
        }



    }

    private int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private double getRandomDouble(double min, double max) {
        double random = new Random().nextDouble();
        return min + (random * (max - min));
    }


    @Test
    public void updateInvoice() {
        var entity = new InvoiceDto();
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        entity.setPaymentStatus(PaymentStatus.UNPAID);

        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockInvoiceEntity));
        when(invoiceRepository.save(any(InvoiceEntity.class))).thenReturn(mockInvoiceEntity);

        ResponseMessage actualResponse = invoiceService.updateInvoice(entity, 1L);
        invoiceService.updateInvoice(entity, 1L);

        assertNotNull(actualResponse);
        assertEquals(actualResponse.getResponseMessage(), "Invoice updated successfully.");
        assertEquals(actualResponse.getHttpStatus(), HttpStatus.ACCEPTED);
    }


    @Test
    public void deleteInvoice() {
        //Invoice status as PAID and more then 1 year old
        mockInvoiceEntity.setPaymentStatus(PaymentStatus.PAID);
        mockInvoiceEntity.setCreatedDate(ZonedDateTime.now().minusMonths(13));

        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockInvoiceEntity));
        ResponseMessage actualResponse = invoiceService.deleteInvoice(1L);

        assertNotNull(actualResponse);
        assertEquals("Invoice deleted successfully.", actualResponse.getResponseMessage());
        assertEquals(HttpStatus.ACCEPTED, actualResponse.getHttpStatus());


        //Invoice status as PAID and less then 1 year old
        mockInvoiceEntity.setPaymentStatus(PaymentStatus.PAID);
        mockInvoiceEntity.setCreatedDate(ZonedDateTime.now().minusMonths(6));

        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockInvoiceEntity));
        actualResponse = invoiceService.deleteInvoice(1L);

        assertNotNull(actualResponse);
        assertEquals("Unpaid/Recent Invoice cannot be deleted.", actualResponse.getResponseMessage());
        assertEquals(NOT_ACCEPTABLE, actualResponse.getHttpStatus());


        //Invoice status as UNPAID and more then 1 year old
        mockInvoiceEntity.setPaymentStatus(PaymentStatus.UNPAID);
        mockInvoiceEntity.setCreatedDate(ZonedDateTime.now().minusMonths(16));

        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(mockInvoiceEntity));
        actualResponse = invoiceService.deleteInvoice(1L);

        assertNotNull(actualResponse);
        assertEquals("Unpaid/Recent Invoice cannot be deleted.", actualResponse.getResponseMessage());
        assertEquals(NOT_ACCEPTABLE, actualResponse.getHttpStatus());


    }
}
