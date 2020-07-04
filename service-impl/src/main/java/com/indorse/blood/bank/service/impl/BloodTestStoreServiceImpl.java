package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodTestStoreRepository;
import com.indorse.blood.bank.model.BloodInventory;
import com.indorse.blood.bank.model.BloodTestStore;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodInventoryDto;
import com.indorse.blood.bank.rest.web.model.BloodRequestDetailDto;
import com.indorse.blood.bank.rest.web.model.BloodTestStoreDto;
import com.indorse.blood.bank.service.api.BloodDonationDetailService;
import com.indorse.blood.bank.service.api.BloodInventoryService;
import com.indorse.blood.bank.service.api.BloodTestStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

import static com.indorse.blood.bank.model.constant.ErrorCode.*;

@Service
public class BloodTestStoreServiceImpl implements BloodTestStoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloodTestStoreServiceImpl.class);
    private static final String BLOOD_TEST_PREFIX = "BT";

    @Autowired
    private BloodTestStoreRepository bloodTestStoreRepository;

    @Autowired
    private BloodInventoryService bloodInventoryService;

    @Autowired
    private BloodDonationDetailService bloodDonationDetailService;

    @Override
    public BloodTestStoreDto add(BloodTestStoreDto bloodTestStoreDto) {
        LOGGER.info("Adding blood test store");
        if (ObjectUtils.isEmpty(bloodTestStoreDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Test Details"});
        }
        if (!ObjectUtils.isEmpty(bloodTestStoreDto.getTestId())) {
            throw new BloodBankException(NEW_ENTITY_WITH_ID_CODE, new Object[]{"Blood Test detail", bloodTestStoreDto.getTestId()});
        }
        BloodTestStore bloodTestStore = new BloodTestStore();
        BeanUtils.copyProperties(bloodTestStoreDto, bloodTestStore);
        if (ObjectUtils.isEmpty(bloodTestStore.getConductedOn())) {
            bloodTestStore.setConductedOn(new Date());
        }
        BloodInventory bloodInventory = bloodInventoryService.getBloodInventoryByInventoryCode(bloodTestStoreDto.getInventoryCode());
        bloodTestStore.setBloodGroup(bloodInventory.getBloodGroup());
        bloodTestStore.setBloodSubType(bloodInventory.getBloodSubType());
        bloodTestStore = bloodTestStoreRepository.save(bloodTestStore);
        bloodTestStore.setTestId(BLOOD_TEST_PREFIX+bloodTestStore.getId());
        bloodTestStore = bloodTestStoreRepository.save(bloodTestStore);
        bloodTestStoreDto.setTestId(bloodTestStore.getTestId());


        BloodInventoryDto bloodInventoryDto= new BloodInventoryDto();
        BeanUtils.copyProperties(bloodInventory, bloodInventoryDto);
        bloodInventoryDto.setBloodBankBranchCode(bloodInventory.getBloodBankBranch().getBranchCode());
        bloodInventoryDto.setDonationUniqueId(bloodInventory.getBloodDonationDetail().getDonationUniqueId());
        bloodInventoryDto.setTestId(bloodTestStoreDto.getTestId());
        bloodInventoryService.update(bloodInventoryDto);
        bloodTestStoreDto.setBloodSubType(bloodTestStore.getBloodSubType());
        bloodTestStoreDto.setBloodGroup(bloodTestStore.getBloodGroup());

        return bloodTestStoreDto;
    }

    @Override
    public void update(BloodTestStoreDto bloodTestStoreDto) {
        LOGGER.info("Updating bloodTestStoreDto {}", bloodTestStoreDto.toString());
        if (ObjectUtils.isEmpty(bloodTestStoreDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"blood bank"});
        }
        BloodTestStore bloodTestStore = bloodTestStoreRepository.findByTestId(bloodTestStoreDto.getTestId());
        if (ObjectUtils.isEmpty(bloodTestStore)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodTestStore", bloodTestStoreDto.getTestId()});
        }
        bloodTestStore.setInfo(bloodTestStoreDto.getInfo());
        bloodTestStore.setConductedOn(bloodTestStoreDto.getConductedOn());
        bloodTestStoreRepository.save(bloodTestStore);
    }

    @Override
    public BloodTestStoreDto getByTestId(String testId) {
        BloodTestStore bloodTestStore = bloodTestStoreRepository.findByTestId(testId);
        if (ObjectUtils.isEmpty(bloodTestStore)){
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodTest Store", testId});
        }
        BloodTestStoreDto bloodTestStoreDto = new BloodTestStoreDto();
        BeanUtils.copyProperties(bloodTestStore, bloodTestStoreDto);
        return bloodTestStoreDto;
    }

    @Override
    public BloodTestStoreDto getByInventoryCode(String inventoryCode) {
        BloodInventory bloodInventory = bloodInventoryService.getBloodInventoryByInventoryCode(inventoryCode);
        BloodTestStore bloodTestStore = bloodInventory.getBloodTestStore();
        if (ObjectUtils.isEmpty(bloodTestStore)){
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodTest Store", inventoryCode});
        }
        BloodTestStoreDto bloodTestStoreDto = new BloodTestStoreDto();
        BeanUtils.copyProperties(bloodTestStore, bloodTestStoreDto);
        return bloodTestStoreDto;
    }

    @Override
    public BloodTestStore getBloodTestStoreByTestId(String testId) {
        return bloodTestStoreRepository.findByTestId(testId);
    }

    @Override
    public void updateTestResult(String testId, boolean passed, String info) {
        LOGGER.info("updating test result with {}", testId);
        BloodTestStore bloodTestStore = bloodTestStoreRepository.findByTestId(testId);
        if (ObjectUtils.isEmpty(bloodTestStore)){
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodTest Store", testId});
        }
        bloodTestStore.setPassed(passed);
        bloodTestStore.setInfo(info);
        if (passed) {
            bloodInventoryService.markInventoryAsActive(testId);
        }
        bloodTestStoreRepository.save(bloodTestStore);
    }
}
