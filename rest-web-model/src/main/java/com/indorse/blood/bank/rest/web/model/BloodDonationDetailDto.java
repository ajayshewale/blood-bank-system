package com.indorse.blood.bank.rest.web.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class BloodDonationDetailDto extends BaseDto{

    private String bloodBankBranchCode;
    private Date donatedOn;
    private BloodGroup bloodGroup;
    private Integer quantityInMl;
    private String memberId;
    private String donationUniqueId;
    private BloodSubType bloodSubType;
    private String inventoryCode;

}
