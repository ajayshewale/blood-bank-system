package com.indorse.blood.bank.rest.web.controller;

import com.indorse.blood.bank.rest.web.model.ApiResponseDto;
import com.indorse.blood.bank.rest.web.model.BloodTestStoreDto;
import com.indorse.blood.bank.service.api.BloodTestStoreService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/blood-test-store")
@Api(description = "Provides operation around blood test store")
public class BloodTestStoreController {

    @Autowired
    private BloodTestStoreService bloodTestStoreService;

    @PostMapping(value = "")
    public ApiResponseDto<BloodTestStoreDto> addBloodToTestStore(HttpServletRequest request,
                                                             @RequestBody BloodTestStoreDto bloodTestStoreDto) {
        BloodTestStoreDto storeDto = bloodTestStoreService.add(bloodTestStoreDto);
        return new ApiResponseDto<BloodTestStoreDto>(storeDto, "Blood added to test store successfully successfully", ApiResponseDto.STATUS_SUCCESS);
    }


    @GetMapping(value = "/{testId}")
    public ApiResponseDto<BloodTestStoreDto>getBloodTestStoreDetail(HttpServletRequest request, @PathVariable String testId) {
        BloodTestStoreDto bloodTestStoreDto = bloodTestStoreService.getByTestId(testId);
        return new ApiResponseDto<BloodTestStoreDto>(bloodTestStoreDto, "Blood Test store details Fetched successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @GetMapping(value = "/inventory/{inventoryCode}")
    public ApiResponseDto<BloodTestStoreDto>getBloodTestStoreDetailbyInventoryCode(HttpServletRequest request, @PathVariable String inventoryCode) {
        BloodTestStoreDto bloodTestStoreDto = bloodTestStoreService.getByInventoryCode(inventoryCode);
        return new ApiResponseDto<BloodTestStoreDto>(bloodTestStoreDto, "Blood Test store details Fetched successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @PutMapping(value = "/{testId}")
    public ApiResponseDto update(HttpServletRequest request, @PathVariable String testId,
                                                @RequestBody BloodTestStoreDto bloodTestStoreDto) {
        bloodTestStoreService.update(bloodTestStoreDto);
        return new ApiResponseDto<>("Blood Test Store detail Updated successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @PutMapping(value = "/result/{testId}")
    public ApiResponseDto updateTestResult(HttpServletRequest request, @PathVariable String testId, @RequestParam boolean passed, @RequestParam String resultInfo) {
        bloodTestStoreService.updateTestResult(testId, passed, resultInfo);
        return new ApiResponseDto<>("Blood Test result updated successfully", ApiResponseDto.STATUS_SUCCESS);
    }

}
