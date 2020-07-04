package com.indorse.blood.bank.rest.web.controller;

import com.indorse.blood.bank.rest.web.model.ApiResponseDto;
import com.indorse.blood.bank.rest.web.model.BloodRequestDetailDto;
import com.indorse.blood.bank.service.api.BloodRequestDetailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/blood-request")
@Api(description = "Provides operation around blood requests")
public class BloodRequestController {

    @Autowired
    private BloodRequestDetailService bloodRequestDetailService;

    @PostMapping(value = "")
    public ApiResponseDto<BloodRequestDetailDto> requestForBlood(HttpServletRequest request, @RequestBody BloodRequestDetailDto bloodRequestDetailDto) {
        BloodRequestDetailDto requestDetailDto = bloodRequestDetailService.requestBlood(bloodRequestDetailDto);
        ApiResponseDto<BloodRequestDetailDto> apiResponseDto;
        if (!ObjectUtils.isEmpty(requestDetailDto)) {
            apiResponseDto = new ApiResponseDto<BloodRequestDetailDto>(requestDetailDto,
                    "Blood request completed", ApiResponseDto.STATUS_SUCCESS);
        } else {
            apiResponseDto = new ApiResponseDto<BloodRequestDetailDto>(null,
                    "Sorry Blood request can not be fulfilled", ApiResponseDto.STATUS_SUCCESS);
        }
        return apiResponseDto;
    }

}
