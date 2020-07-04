package com.indorse.blood.bank.rest.web.model;

import lombok.Data;

@Data
public class BloodBankBranchDto extends BaseDto {

    private String branchName;
    private String country;
    private String state;
    private String city;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String phoneNumber;
    private String bloodBankName;
    private String branchCode;
}
