package com.indorse.blood.bank.rest.web.model;

import lombok.Data;

@Data
public class ApiResponseDto<E> {

    public static String STATUS_SUCCESS = "success";
    public static String STATUS_ERROR = "error";
    private E data;
    private String message;
    private String status;

    public ApiResponseDto(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public ApiResponseDto(E data, String message, String status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }
}