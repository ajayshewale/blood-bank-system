package com.indorse.blood.bank.rest.web.controller;

import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.ApiResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@ControllerAdvice(basePackages = "com.indorse.blood.bank.rest.web.controller", annotations = RestController.class)
public class ExceptionHandlingController {
    private static Logger LOG = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @Autowired
    private MessageSource messageSource;


    @ExceptionHandler({BloodBankException.class})
    @ResponseBody
    public ApiResponseDto bloodBankExceptionHandler(BloodBankException e, Locale locale) {
        LOG.error("Error : ", e);
        String message = messageSource.getMessage(e.getErrorCode().getMessageKey(), e.getParams(), locale);
        return new ApiResponseDto( message, ApiResponseDto.STATUS_ERROR);
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ApiResponseDto genericExceptionHandler(Exception e) {
        LOG.error("Error : ", e);
        return new ApiResponseDto("Unknown Error occurred", ApiResponseDto.STATUS_ERROR);
    }
}
