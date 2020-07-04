package com.indorse.blood.bank.rest.web.controller;

import com.indorse.blood.bank.rest.web.model.ApiResponseDto;
import com.indorse.blood.bank.rest.web.model.MemberDto;
import com.indorse.blood.bank.service.api.MemberService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/member")
@Api(description = "Provides operation around blood bank member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping(value = "")
    public ApiResponseDto<MemberDto> addMember(HttpServletRequest request, @RequestBody MemberDto memberDto){
        MemberDto member = memberService.add(memberDto);
        return new ApiResponseDto<MemberDto>(member, "Member Added successfully", ApiResponseDto.STATUS_SUCCESS);
    }


    @GetMapping(value = "")
    public ApiResponseDto<MemberDto> getMember(HttpServletRequest request, @RequestParam(required = false) String email,
                                               @RequestParam(required = false) String memberId){
        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(memberId)){
            new ApiResponseDto<MemberDto>(null, "No member with null email and Id", ApiResponseDto.STATUS_SUCCESS);
        }
        MemberDto member = memberService.getByMemberIdOrEmail(memberId, email);
        return new ApiResponseDto<MemberDto>(member, "Member Fetched successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @PutMapping(value = "/{memberId}")
    public ApiResponseDto<MemberDto> updateMember(HttpServletRequest request, @PathVariable String memberId, @RequestBody MemberDto memberDto){
        memberService.update(memberDto);
        return new ApiResponseDto<>( "Member Updated successfully", ApiResponseDto.STATUS_SUCCESS);
    }

    @DeleteMapping (value = "")
    public ApiResponseDto<MemberDto> deleteMember(HttpServletRequest request, @RequestParam(required = false) String email,
                                                  @RequestParam(required = false) String memberId){
        memberService.deleteByMemberIdOrEmail(memberId, email);
        return new ApiResponseDto<>( "Member Deleted successfully", ApiResponseDto.STATUS_SUCCESS);
    }
}
