package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodBankBranchRepository;
import com.indorse.blood.bank.model.BloodBank;
import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodBankBranchDto;
import com.indorse.blood.bank.service.api.BloodBankBranchService;
import com.indorse.blood.bank.service.api.BloodBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.indorse.blood.bank.model.constant.ErrorCode.*;

@Service
public class BloodBankBranchServiceImpl implements BloodBankBranchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloodBankBranchServiceImpl.class);

    public static final String BRANCH_CODE_PREFIX = "BR";

    @Autowired
    private BloodBankBranchRepository bloodBankBranchRepository;

    @Autowired
    private BloodBankService bloodBankService;


    @Override
    public BloodBankBranchDto add(BloodBankBranchDto bloodBankBranchDto) {
        LOGGER.info("BloodBank branch = {} ", bloodBankBranchDto);
        if (ObjectUtils.isEmpty(bloodBankBranchDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Bank branch"});
        }
        BloodBank bloodBank = bloodBankService.getBloodBankModelByName(bloodBankBranchDto.getBloodBankName());
        if (ObjectUtils.isEmpty(bloodBank)) {
            throw new BloodBankException(BLOOD_BANK_NOT_EXIST, new Object[]{bloodBankBranchDto.getBloodBankName()});
        }
        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        BeanUtils.copyProperties(bloodBankBranchDto, bloodBankBranch);
        bloodBankBranch.setBloodBank(bloodBank);
        bloodBankBranch = bloodBankBranchRepository.save(bloodBankBranch);
        bloodBankBranch.setBranchCode(bloodBank.getBankCode() + BRANCH_CODE_PREFIX + bloodBankBranch.getId());
        bloodBankBranch = bloodBankBranchRepository.save(bloodBankBranch);
        bloodBankBranchDto.setBranchCode(bloodBankBranch.getBranchCode());
        return bloodBankBranchDto;
    }

    @Override
    public void update(BloodBankBranchDto bloodBankBranchDto) {
        LOGGER.info("Updating BloodBankBranch =  ", bloodBankBranchDto.getBranchName());
        if (ObjectUtils.isEmpty(bloodBankBranchDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"blood bank branch"});
        }
        BloodBankBranch bloodBankBranch = bloodBankBranchRepository.findByBranchCode(bloodBankBranchDto.getBranchCode());
        if (ObjectUtils.isEmpty(bloodBankBranch)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodBankBranch", bloodBankBranch});
        }
        updateFields(bloodBankBranch, bloodBankBranchDto);
        BloodBank bloodBank = bloodBankService.getBloodBankModelByName(bloodBankBranchDto.getBloodBankName());
        if (ObjectUtils.isEmpty(bloodBankBranchDto)) {
            throw new BloodBankException(BLOOD_BANK_NOT_EXIST, new Object[]{bloodBankBranchDto.getBloodBankName()});
        }
        bloodBankBranch.setBloodBank(bloodBank);
        bloodBankBranchRepository.save(bloodBankBranch);
    }

    private void updateFields(BloodBankBranch bloodBankBranch, BloodBankBranchDto bloodBankBranchDto) {
        bloodBankBranch.setBranchName(bloodBankBranchDto.getBranchName());
        bloodBankBranch.setState(bloodBankBranchDto.getState());
        bloodBankBranch.setPostalCode(bloodBankBranchDto.getPostalCode());
        bloodBankBranch.setPhoneNumber(bloodBankBranchDto.getPhoneNumber());
        bloodBankBranch.setCountry(bloodBankBranchDto.getCountry());
        bloodBankBranch.setCity(bloodBankBranchDto.getCity());
        bloodBankBranch.setBranchName(bloodBankBranchDto.getBranchName());
        bloodBankBranch.setAddressLine2(bloodBankBranchDto.getAddressLine2());
        bloodBankBranch.setAddressLine1(bloodBankBranchDto.getAddressLine1());
    }

    @Override
    public BloodBankBranchDto getByBranchCode(String branchCode) {
        LOGGER.info("Fetching BloodBankBranch by branchCode = {}", branchCode);

        BloodBankBranch bloodBankBranch = bloodBankBranchRepository.findByBranchCode(branchCode);
        BloodBankBranchDto bloodBankBranchDto = new BloodBankBranchDto();
        BeanUtils.copyProperties(bloodBankBranch, bloodBankBranchDto);
        return bloodBankBranchDto;
    }

    @Override
    public List<BloodBankBranchDto> getAllBloodBankBranches(String bloodBankName) {
        List<BloodBankBranchDto> bloodBankBranchDtos = new ArrayList<>();
        List<BloodBankBranch> branches = bloodBankBranchRepository.findAllByBloodBankName(bloodBankName);
        for (BloodBankBranch bloodBankBranch : branches) {
            BloodBankBranchDto bloodBankBranchDto = new BloodBankBranchDto();
            BeanUtils.copyProperties(bloodBankBranch, bloodBankBranchDto);
            bloodBankBranchDtos.add(bloodBankBranchDto);
        }
        return bloodBankBranchDtos;
    }

    @Override
    public void deleteAllBloodBankBranches(String bloodBankName) {
        List<BloodBankBranchDto> bloodBankBranchDtos = new ArrayList<>();
        List<BloodBankBranch> branches = bloodBankBranchRepository.findAllByBloodBankName(bloodBankName);
        for (BloodBankBranch bloodBankBranch : branches) {
            bloodBankBranch.setMarkForDelete(true);
            bloodBankBranchRepository.save(bloodBankBranch);
        }
    }

    @Override
    public void deleteByBranchCode(String branchCode) {
        LOGGER.info("Deleting BloodBankBranch by branchCode = {}", branchCode);
        BloodBankBranch bloodBankBranch = bloodBankBranchRepository.findByBranchCode(branchCode);
        if (ObjectUtils.isEmpty(bloodBankBranch)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND,
                    new Object[]{"Blood Bank Branch", branchCode});
        }
        bloodBankBranch.setMarkForDelete(true);
        bloodBankBranchRepository.save(bloodBankBranch);
    }

    @Override
    public BloodBankBranch getBloodBankBranchModelByBranchCode(String branchCode){
        return bloodBankBranchRepository.findByBranchCode(branchCode);
    }
}
