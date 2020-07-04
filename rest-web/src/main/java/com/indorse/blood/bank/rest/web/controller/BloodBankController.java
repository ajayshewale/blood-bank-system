package com.indorse.blood.bank.rest.web.controller;

import com.indorse.blood.bank.rest.web.model.ApiResponseDto;
import com.indorse.blood.bank.rest.web.model.BloodBankDto;
import com.indorse.blood.bank.service.api.BloodBankService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/blood-bank")
@Api(description = "Provides operation around blood bank ")
public class BloodBankController {

    @Autowired
    private BloodBankService bloodBankService;

    @PostMapping(value = "")
    public ApiResponseDto<BloodBankDto> addBloodBank(HttpServletRequest request, @RequestBody BloodBankDto bloodBankDto){
        BloodBankDto bloodBank = bloodBankService.save(bloodBankDto);
        return new ApiResponseDto<BloodBankDto>(bloodBank, "Blood Bank Added successfully", ApiResponseDto.STATUS_SUCCESS);
    }


    @GetMapping(value = "")
    public ApiResponseDto<BloodBankDto> getBloodBank(HttpServletRequest request,@RequestParam String bloodBankName){
        BloodBankDto bloodBankDto = bloodBankService.getByName(bloodBankName);
        return new ApiResponseDto<BloodBankDto>(bloodBankDto, "Blood Bank details Fetched successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @PutMapping(value = "/{bankCode}")
    public ApiResponseDto<BloodBankDto> updateBloodBank(HttpServletRequest request, @PathVariable String bankCode, @RequestBody BloodBankDto bloodBankDto){
        bloodBankService.update(bloodBankDto);
        return new ApiResponseDto<>( "Blood Bank Details Updated successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @DeleteMapping (value = "")
    public ApiResponseDto<BloodBankDto> deleteBloodBank(HttpServletRequest request, @RequestParam(required = false) String bankName,
                                                  @RequestParam(required = false) String bankCode){
        bloodBankService.deleteByNameOrBankCode(bankName, bankCode);
        return new ApiResponseDto<>( "Blood bank Deleted successfully", ApiResponseDto.STATUS_SUCCESS);
    }
}
