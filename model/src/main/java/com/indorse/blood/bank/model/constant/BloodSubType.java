package com.indorse.blood.bank.model.constant;

public enum BloodSubType {
    WHOLE("WHOLE", "whole", 35),
    RBC("RBC", "Red Blood Cells", 35),
    FFP("FFP", "Red Blood Cells", 365),
    PC("PC", "Platelet concentrate", 5);

    private String type;
    private String description;
    private Integer expiryInDays;

    BloodSubType(String type, String description, Integer expiryInDays) {
        this.type= type;
        this.description = description;
        this.expiryInDays = expiryInDays;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Integer getExpiryInDays() {
        return expiryInDays;
    }
}
