package com.cognizant.javapunditsinvoicify.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="COMPANY")
public class Company {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String name;
    String address;
    String contactName;
    String contactTitle;
    String contactNumber;
    String invoices;

}
