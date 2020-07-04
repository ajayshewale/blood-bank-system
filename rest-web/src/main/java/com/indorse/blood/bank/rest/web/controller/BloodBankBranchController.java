package com.indorse.blood.bank.rest.web.controller;

import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.rest.web.model.ApiResponseDto;
import com.indorse.blood.bank.rest.web.model.BloodBankBranchDto;
import com.indorse.blood.bank.service.api.BloodBankBranchService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v1/blood-bank-branch")
@Api(description = "Provides operation around blood bank branch ")
public class BloodBankBranchController {

    @Autowired
    private BloodBankBranchService bloodBankBranchService;

    @PostMapping(value = "")
    public ApiResponseDto<BloodBankBranchDto> addBloodBankBranch(HttpServletRequest request, @RequestBody BloodBankBranchDto bloodBankBranchDto){
        BloodBankBranchDto bankBranchDto = bloodBankBranchService.add(bloodBankBranchDto);
        return new ApiResponseDto<BloodBankBranchDto>(bankBranchDto, "Blood Bank Branch Added successfully", ApiResponseDto.STATUS_SUCCESS);
    }


    @GetMapping(value = "")
    public ApiResponseDto<BloodBankBranchDto> getBloodBankBranch(HttpServletRequest request,@RequestParam String branchCode){
        BloodBankBranchDto bloodBankDto = bloodBankBranchService.getByBranchCode(branchCode);
        return new ApiResponseDto<BloodBankBranchDto>(bloodBankDto, "Blood Bank Branch details Fetched successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @GetMapping(value = "/all")
    public ApiResponseDto<List<BloodBankBranchDto>> getAllBloodBankBranch(HttpServletRequest request, @RequestParam String bankName){
        List<BloodBankBranchDto> bloodBankDtos = bloodBankBranchService.getAllBloodBankBranches(bankName);
        return new ApiResponseDto<List<BloodBankBranchDto>>(bloodBankDtos, "Blood Bank Branch details Fetched successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @PutMapping(value = "/{bankBranchCode}")
    public ApiResponseDto updateBloodBankBranch(HttpServletRequest request, @PathVariable String bankBranchCode, @RequestBody BloodBankBranchDto bloodBankBranchDto){
        bloodBankBranchService.update(bloodBankBranchDto);
        return new ApiResponseDto<>( "Blood Bank branch Details Updated successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @DeleteMapping (value = "/{branchCode}")
    public ApiResponseDto deleteBloodBankBranch(HttpServletRequest request, @PathVariable String branchCode ){
        bloodBankBranchService.deleteByBranchCode(branchCode);
        return new ApiResponseDto<>( "Blood bank branch Deleted successfully", ApiResponseDto.STATUS_SUCCESS);
    }
}
