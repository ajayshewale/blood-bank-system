package com.indorse.blood.bank.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "blood_donation_detail")
public class BloodDonationDetail extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "donor_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private BloodBankBranch bloodBankBranch;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column
    private Date donatedOn;
    @Column
    private BloodGroup bloodGroup;
    @Column
    private BloodSubType bloodSubType;
    @Column
    private Integer quantityInMl;
    @Column(unique = true)
    private String donationUniqueId;

}
