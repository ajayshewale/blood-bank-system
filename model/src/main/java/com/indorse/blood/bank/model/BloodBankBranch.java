package com.indorse.blood.bank.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "blood_bank_branch")
public class BloodBankBranch extends BaseEntity{

    @Column
    private String branchName;
    @Column(unique = true)
    private String branchCode;
    @Column
    private String country;
    @Column
    private String state;
    @Column
    private String city;
    @Column
    private String postalCode;
    @Column
    private String addressLine1;
    @Column
    private String addressLine2;
    @Column
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "blood_bank_id")
    private BloodBank bloodBank;
}
