package com.indorse.blood.bank.rest.web.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import lombok.Data;

import java.util.Date;

@Data
public class BloodTestStoreDto {

    private Boolean passed;
    private String info;
    private Date conductedOn;
    private String testId;
    private BloodGroup bloodGroup;
    private BloodSubType bloodSubType;
    private String inventoryCode;
}
