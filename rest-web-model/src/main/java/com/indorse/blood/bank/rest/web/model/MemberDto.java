package com.indorse.blood.bank.rest.web.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import lombok.Data;

import java.util.Date;

@Data
public class MemberDto extends BaseDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private BloodGroup bloodGroup;
    private Date dateOfBirth;
    private String email;
    private String memberId;
}
