package com.indorse.blood.bank.rest.web.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class BloodInventoryDto extends BaseDto{

    private String bloodBankBranchCode;
    private BloodGroup bloodGroup;
    private BloodSubType bloodSubType;
    private Integer quantityInMl;
    private Date expiresOn;
    private String inventoryCode;
    private String donationUniqueId;
    private String testId;


}
