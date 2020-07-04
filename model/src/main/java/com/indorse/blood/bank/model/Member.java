package com.indorse.blood.bank.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "member")
public class Member extends BaseEntity {

    @Column
    private String firstName;
    @Column
    private String middleName;
    @Column
    private String lastName;
    @Column
    private BloodGroup bloodGroup;
    @Column
    private Date dateOfBirth;
    @Column
    private String email;
    @Column(unique = true)
    private String memberId;
}
