package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodRequestDetailRepository;
import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.model.BloodInventory;
import com.indorse.blood.bank.model.BloodRequestDetail;
import com.indorse.blood.bank.model.Member;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodInventoryDto;
import com.indorse.blood.bank.rest.web.model.BloodRequestDetailDto;
import com.indorse.blood.bank.service.api.BloodBankBranchService;
import com.indorse.blood.bank.service.api.BloodInventoryService;
import com.indorse.blood.bank.service.api.BloodRequestDetailService;
import com.indorse.blood.bank.service.api.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static com.indorse.blood.bank.model.constant.ErrorCode.RESOURCE_NOT_FOUND;

@Service
public class BloodRequestDetailServiceImpl implements BloodRequestDetailService {

    private static final String BLOOD_REQUEST_PREFIX = "BR";
    private static final Logger LOGGER = LoggerFactory.getLogger(BloodRequestDetailServiceImpl.class);

    @Autowired
    private BloodRequestDetailRepository bloodRequestDetailRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BloodBankBranchService bloodBankBranchService;
    @Autowired
    private BloodInventoryService bloodInventoryService;

    @Override
    public BloodRequestDetailDto add(BloodRequestDetailDto bloodRequestDetailDto) {

        LOGGER.info("Adding Blood Request with member = {}", bloodRequestDetailDto);
        if (ObjectUtils.isEmpty(bloodRequestDetailDto)) {

            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Request"});
        }
        BloodRequestDetail bloodRequestDetail = new BloodRequestDetail();
        BeanUtils.copyProperties(bloodRequestDetailDto, bloodRequestDetail);
        Member member = memberService.getByMemberId(bloodRequestDetailDto.getMemberId());
        if (ObjectUtils.isEmpty(member)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Member", bloodRequestDetailDto.getMemberId()});
        }
        bloodRequestDetail.setMember(member);
        BloodBankBranch bloodBankBranch =
                bloodBankBranchService.getBloodBankBranchModelByBranchCode(bloodRequestDetailDto.getRequestedFromBranchCode());
        if (ObjectUtils.isEmpty(bloodBankBranch)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood Bank Branch", bloodRequestDetailDto.getRequestedFromBranchCode()});
        }
        bloodRequestDetail.setRequestedFromBranch(bloodBankBranch);
        bloodRequestDetail.setRequestCompleted(false);
        bloodRequestDetail = bloodRequestDetailRepository.save(bloodRequestDetail);
        bloodRequestDetail.setRequestId(BLOOD_REQUEST_PREFIX + bloodRequestDetail.getId());
        bloodRequestDetail = bloodRequestDetailRepository.save(bloodRequestDetail);
        bloodRequestDetailDto.setBloodRequestId(bloodRequestDetail.getRequestId());
        return bloodRequestDetailDto;

    }

    @Override
    public void update(BloodRequestDetailDto bloodRequestDetailDto) {
        LOGGER.info("Adding Blood Request with member = {}", bloodRequestDetailDto);
        if (ObjectUtils.isEmpty(bloodRequestDetailDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Request"});
        }
        BloodRequestDetail bloodRequestDetail = bloodRequestDetailRepository.findByRequestId(bloodRequestDetailDto.getBloodRequestId());
        if (ObjectUtils.isEmpty(bloodRequestDetail)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"bloodRequestDetail", bloodRequestDetailDto.toString()});
        }

        Member member = memberService.getByMemberId(bloodRequestDetailDto.getMemberId());
        if (ObjectUtils.isEmpty(member)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Member", bloodRequestDetailDto.getMemberId()});
        }
        bloodRequestDetail.setMember(member);
        BloodBankBranch requestedBloodBankBranch =
                bloodBankBranchService.getBloodBankBranchModelByBranchCode(bloodRequestDetailDto.getRequestedFromBranchCode());
        if (ObjectUtils.isEmpty(requestedBloodBankBranch)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood Bank Branch", bloodRequestDetailDto.getRequestedFromBranchCode()});
        }
        bloodRequestDetail.setRequestedFromBranch(requestedBloodBankBranch);

        BloodBankBranch givenBloodBankBranch =
                bloodBankBranchService.getBloodBankBranchModelByBranchCode(bloodRequestDetailDto.getRequestedFromBranchCode());
        if (ObjectUtils.isEmpty(givenBloodBankBranch)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Given Blood Bank Branch", bloodRequestDetailDto.getGivenFromBranchCode()});
        }
        bloodRequestDetail.setGivenFromBranch(givenBloodBankBranch);
        if (!ObjectUtils.isEmpty(bloodRequestDetailDto.getInventoryCode())) {
            BloodInventory bloodInventory = bloodInventoryService.getBloodInventoryByInventoryCode(bloodRequestDetailDto.getInventoryCode());
            if (ObjectUtils.isEmpty(bloodInventory)) {
                throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"bloodInventory", bloodRequestDetailDto.getInventoryCode()});
            }
            bloodRequestDetail.setBloodInventory(bloodInventory);
        }

        bloodRequestDetail.setRequestCompleted(bloodRequestDetailDto.isRequestCompleted());
        bloodRequestDetail.setBloodGroup(bloodRequestDetailDto.getBloodGroup());
        bloodRequestDetail.setBloodSubType(bloodRequestDetailDto.getBloodSubType());
        bloodRequestDetail.setGivenOn(bloodRequestDetailDto.getGivenOn());
        bloodRequestDetail.setInfo(bloodRequestDetailDto.getInfo());
        bloodRequestDetail.setQuantityInMl(bloodRequestDetailDto.getQuantityInMl());
        bloodRequestDetailRepository.save(bloodRequestDetail);
    }

    @Override
    public BloodRequestDetailDto getBloodRequestDetail(String bloodRequestId) {
        LOGGER.info("Fetching blood Request detail by bloodRequestId = {}", bloodRequestId);
        BloodRequestDetailDto bloodRequestDetailDto = new BloodRequestDetailDto();
        BloodRequestDetail bloodRequestDetail = bloodRequestDetailRepository.findByRequestId(bloodRequestId);
        if (ObjectUtils.isEmpty(bloodRequestDetail)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood Request detail", bloodRequestDetailDto.getRequestedFromBranchCode()});
        }
        BeanUtils.copyProperties(bloodRequestDetail, bloodRequestDetailDto);
        return bloodRequestDetailDto;
    }

    @Override
    public void deleteBloodRequestDetailById(String bloodRequestId) {
        BloodRequestDetailDto bloodRequestDetailDto = new BloodRequestDetailDto();
        BloodRequestDetail bloodRequestDetail = bloodRequestDetailRepository.findByRequestId(bloodRequestId);
        if (ObjectUtils.isEmpty(bloodRequestDetail)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood Request detail", bloodRequestDetailDto.getRequestedFromBranchCode()});
        }
        bloodRequestDetail.setMarkForDelete(true);
        bloodRequestDetailRepository.save(bloodRequestDetail);
    }

    @Override
    public BloodRequestDetailDto requestBlood(BloodRequestDetailDto bloodRequestDetailDto) {
        BloodRequestDetailDto requestDetailDto = this.add(bloodRequestDetailDto);
        BloodInventoryDto bloodInventoryDto = bloodInventoryService.getBlood(bloodRequestDetailDto.getBloodGroup(),
                bloodRequestDetailDto.getBloodSubType(), bloodRequestDetailDto.getQuantityInMl());
        if (!ObjectUtils.isEmpty(bloodInventoryDto)){
            requestDetailDto.setInventoryCode(bloodInventoryDto.getInventoryCode());
            requestDetailDto.setGivenFromBranchCode(bloodInventoryDto.getBloodBankBranchCode());
            requestDetailDto.setRequestCompleted(true);
            requestDetailDto.setGivenOn(new Date());
            this.update(requestDetailDto);
        } else {
            requestDetailDto = null;
        }
        return requestDetailDto;
    }

}
