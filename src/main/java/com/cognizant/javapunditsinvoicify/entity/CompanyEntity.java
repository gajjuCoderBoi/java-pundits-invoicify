package com.cognizant.javapunditsinvoicify.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Builder
@Table(name="company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique = true)
    String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;

    String contactName;
    String contactTitle;
    Integer contactNumber;
    String invoices;

    public Long getId() {
        return id;
    }

    public CompanyEntity(String name, AddressEntity addressEntity, String contactName, String contactTitle, Integer contactNumber, String invoices) {
        this.name = name;
        this.addressEntity = addressEntity;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.contactNumber = contactNumber;
        this.invoices = invoices;
    }
}
