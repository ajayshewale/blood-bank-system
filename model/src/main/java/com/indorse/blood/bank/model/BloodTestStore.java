package com.indorse.blood.bank.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "blood_test_store")
public class BloodTestStore extends BaseEntity {

    @Column
    private Boolean passed;
    @Column
    private String info;
    @Column
    private Date conductedOn;
    @Column(unique = true)
    private String testId;
    @Column
    private BloodGroup bloodGroup;
    @Column
    private BloodSubType bloodSubType;
}
