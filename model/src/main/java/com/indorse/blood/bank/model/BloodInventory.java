package com.indorse.blood.bank.model;

import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "blood_inventory")
public class BloodInventory extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "stored_at")
    private BloodBankBranch bloodBankBranch;
    @Column
    private BloodGroup bloodGroup;
    @Column
    private BloodSubType bloodSubType;
    @Column
    private Integer quantityInMl;
    @Column
    private Date expiresOn;
    @Column
    private Boolean active;
    @Column(unique = true)
    private String inventoryCode;
    @OneToOne
    @JoinColumn(name = "donation_detail_id")
    private BloodDonationDetail bloodDonationDetail;
    @OneToOne
    @JoinColumn(name = "test_store_id")
    private BloodTestStore bloodTestStore;
}
