package com.indorse.blood.bank.model.constant;

public enum BloodGroup {
    AP("A+", "Blood group A positive"),
    AN("A-", "Blood group A negative"),
    BP("B+", "Blood group B positive"),
    BN("B-", "Blood group B Negative"),
    ABP("ABP", "Blood group AB positive"),
    ABN("ABN", "Blood group AB negative"),
    OP("O+", "Blood group O positive"),
    ON("O-", "Blood group O negative");



    private String group;
    private String description;

    BloodGroup(String group, String description) {
        this.group = group;
        this.description = description;
    }
}
