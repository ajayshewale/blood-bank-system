package com.indorse.blood.bank.model.constant;

public enum BloodStorageType {
    WHOLE("WHOLE", "whole", 35),
    RBC("RBC", "Red Blood Cells", 35),
    FFP("FFP", "Red Blood Cells", 365),
    PC("PC", "Platelet concentrate", 5);

    private String type;
    private String description;
    private Integer expiryInDays;

    BloodStorageType(String type, String description, Integer epiryInDays) {
        this.type= type;
        this.description = description;
        this.expiryInDays=epiryInDays;
    }
}
