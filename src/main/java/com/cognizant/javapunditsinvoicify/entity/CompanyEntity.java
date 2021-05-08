package com.cognizant.javapunditsinvoicify.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name="company")
public class CompanyEntity {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;

    String contactName;
    String contactTitle;
    Integer contactNumber;
    String invoices;

}
