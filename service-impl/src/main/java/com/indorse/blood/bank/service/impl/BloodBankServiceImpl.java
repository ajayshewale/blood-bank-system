package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodBankRepository;
import com.indorse.blood.bank.model.BloodBank;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodBankDto;
import com.indorse.blood.bank.service.api.BloodBankBranchService;
import com.indorse.blood.bank.service.api.BloodBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static com.indorse.blood.bank.model.constant.ErrorCode.RESOURCE_NOT_FOUND;

@Service
public class BloodBankServiceImpl implements BloodBankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloodBankServiceImpl.class);
    protected static final String BLOOD_BANK_PREFIX = "BB";

    @Autowired
    private BloodBankRepository bloodBankRepository;
    @Autowired
    private BloodBankBranchService bloodBankBranchService;

    @Override
    public BloodBankDto save(BloodBankDto bloodBankDto) {
        LOGGER.info("Adding Blood Bank with name = {}", bloodBankDto);
        if (ObjectUtils.isEmpty(bloodBankDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Bank"});
        }
        BloodBank bloodBank = new BloodBank();
        BeanUtils.copyProperties(bloodBankDto, bloodBank);
        bloodBank = bloodBankRepository.save(bloodBank);
        bloodBank.setBankCode(BLOOD_BANK_PREFIX + bloodBank.getId());
        bloodBank = bloodBankRepository.save(bloodBank);
        bloodBankDto.setBankCode(bloodBank.getBankCode());
        return bloodBankDto;
    }

    @Override
    public void update(BloodBankDto bloodBankDto) {
        LOGGER.info("Updating blood bank {}", bloodBankDto);
        if (ObjectUtils.isEmpty(bloodBankDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"blood bank"});
        }
        BloodBank bloodBank = bloodBankRepository.findByBankCode(bloodBankDto.getBankCode());
        if (ObjectUtils.isEmpty(bloodBank)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodBank", bloodBank.getBankCode() + ", "
                    + bloodBank.getName()});
        }
        bloodBank.setName(bloodBankDto.getName());
        bloodBankRepository.save(bloodBank);
    }

    @Override
    public BloodBankDto getByName(String name) {
        LOGGER.info("Fetching blood bank {}", name);
        BloodBankDto bloodBankDto = new BloodBankDto();
        BloodBank bloodBank = bloodBankRepository.findByName(name);
        BeanUtils.copyProperties(bloodBank, bloodBankDto);
        return bloodBankDto;
    }

    @Override
    public void deleteByNameOrBankCode(String bloodBankName, String bloodBankCode) {
        LOGGER.info("Deleting blood bank {}", bloodBankCode);
        BloodBank bloodBank = bloodBankRepository.findByBankCodeOrName(bloodBankCode, bloodBankName);
        if (ObjectUtils.isEmpty(bloodBank)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND,
                    new Object[]{"Blood Bank", bloodBank + " and " + bloodBankCode});
        }
        bloodBankBranchService.deleteAllBloodBankBranches(bloodBank.getName());
        bloodBank.setMarkForDelete(true);
        bloodBankRepository.save(bloodBank);
    }

    @Override
    public BloodBank getBloodBankModelByName(String name) {
        return bloodBankRepository.findByBankCodeOrName(null, name);
    }
}
