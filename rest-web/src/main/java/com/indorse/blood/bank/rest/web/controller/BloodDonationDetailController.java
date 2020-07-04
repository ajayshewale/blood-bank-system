package com.indorse.blood.bank.rest.web.controller;

import com.indorse.blood.bank.rest.web.model.ApiResponseDto;
import com.indorse.blood.bank.rest.web.model.BloodDonationDetailDto;
import com.indorse.blood.bank.rest.web.model.DonorStatDto;
import com.indorse.blood.bank.rest.web.model.MemberDto;
import com.indorse.blood.bank.rest.web.model.constant.StatsPeriod;
import com.indorse.blood.bank.service.api.BloodDonationDetailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v1/blood-donation")
@Api(description = "Provides operation around blood donation detail")
public class BloodDonationDetailController {

    @Autowired
    private BloodDonationDetailService bloodDonationDetailService;

    @PostMapping(value = "")
    public ApiResponseDto<BloodDonationDetailDto> addDonatedBlood(HttpServletRequest request,
                                                                  @RequestBody BloodDonationDetailDto bloodDonationDetailDto) {
        BloodDonationDetailDto donationDetailDto = bloodDonationDetailService.save(bloodDonationDetailDto);
        return new ApiResponseDto<BloodDonationDetailDto>(donationDetailDto, "Blood Donated successfully", ApiResponseDto.STATUS_SUCCESS);
    }


    @GetMapping(value = "/{memberId}")
    public ApiResponseDto<List<BloodDonationDetailDto>> getDonatedBloodDetail(HttpServletRequest request, @PathVariable String memberId) {
        List<BloodDonationDetailDto> donationDetailDtoList = bloodDonationDetailService.getDetailsByMemberId(memberId);
        return new ApiResponseDto<List<BloodDonationDetailDto>>(donationDetailDtoList, "Blood Donation details Fetched successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @PutMapping(value = "/{donationUniqueId}")
    public ApiResponseDto updateBloodDonationDetail(HttpServletRequest request, @PathVariable String donationUniqueId,
                                                @RequestBody BloodDonationDetailDto bloodDonationDetailDto) {
        bloodDonationDetailService.update(bloodDonationDetailDto);
        return new ApiResponseDto<>("Blood donation Details Updated successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @DeleteMapping(value = "/{donationUniqueId}")
    public ApiResponseDto deleteBloodDonation(HttpServletRequest request, @PathVariable String donationUniqueId) {
        bloodDonationDetailService.delete(donationUniqueId);
        return new ApiResponseDto<>("Blood Donation detail Deleted successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @GetMapping(value = "/top-donors")
    public ApiResponseDto<List<DonorStatDto>> getTopDonorByPeriod(HttpServletRequest request, @RequestParam StatsPeriod period,
                                                                  @RequestParam(required = false) Integer month, @RequestParam(required = false) Integer year,
                                                                  @RequestParam(required = false, defaultValue = "3") Integer limit) {
        return new ApiResponseDto<List<DonorStatDto>>(bloodDonationDetailService.getTopDonorsByPeriod(period, month, year, limit)
                , "Top donor list Fetched successfully", ApiResponseDto.STATUS_SUCCESS);
    }
}
