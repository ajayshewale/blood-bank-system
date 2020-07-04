package com.indorse.blood.bank.model.exception;

import com.indorse.blood.bank.model.constant.ErrorCode;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BloodBankException extends RuntimeException{

    private ErrorCode errorCode;
    private Object[] params;

    public BloodBankException(ErrorCode errorCode, Object[] params) {
        super();
        this.errorCode = errorCode;
        this.params = params;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("errorCode", errorCode)
                .append("errorMessage", super.getMessage()).toString();
    }
}
