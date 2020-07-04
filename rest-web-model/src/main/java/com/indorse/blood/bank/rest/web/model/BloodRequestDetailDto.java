package com.indorse.blood.bank.rest.web.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import lombok.Data;

import java.util.Date;

@Data
public class BloodRequestDetailDto {
    private String bloodRequestId;
    private String memberId;
    private String requestedFromBranchCode;
    private String givenFromBranchCode;
    private BloodGroup bloodGroup;
    private BloodSubType bloodSubType;
    private Integer quantityInMl;
    private String inventoryCode;
    private boolean requestCompleted;
    private Date givenOn;
    private String info;
}
