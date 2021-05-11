package com.cognizant.javapunditsinvoicify.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String line1;
    String line2;
    String city;
    String state;
    Integer zip;
}
