package com.indorse.blood.bank.model.constant;

public enum  ErrorCode {
    NEW_ENTITY_WITH_ID_CODE("new.entity.with.id.error"),
    CRUD_EMPTY_ENTITY_ERROR("crud.empty.entity.error"),
    BLOOD_BANK_NOT_EXIST("blood.bank.not.exists"),

    RESOURCE_NOT_FOUND("resource.not.found.error"),
    INSUFFICIENT_BLOOD_MSG("insufficient.blood.msg");




    private String messageKey;

    ErrorCode(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getCode() {
        return this.name();
    }

    public String getMessageKey() {
        return this.messageKey;
    }
}
