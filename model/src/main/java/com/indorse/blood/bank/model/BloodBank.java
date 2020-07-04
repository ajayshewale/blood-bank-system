package com.indorse.blood.bank.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "blood_bank")
public class BloodBank extends BaseEntity{

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String bankCode;
}
