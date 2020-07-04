package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodDonationDetailRepository;
import com.indorse.blood.bank.dao.api.BloodInventoryRepository;
import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.model.BloodDonationDetail;
import com.indorse.blood.bank.model.BloodInventory;
import com.indorse.blood.bank.model.BloodTestStore;
import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodInventoryDto;
import com.indorse.blood.bank.service.api.BloodBankBranchService;
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
public class BloodInventoryServiceImpl implements BloodInventoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloodTestStoreServiceImpl.class);
    protected static final String BLOOD_INVENTORY_PREFIX = "BI";

    @Autowired
    private BloodInventoryRepository bloodInventoryRepository;
    @Autowired
    private BloodBankBranchService bloodBankBranchService;
    @Autowired
    private BloodDonationDetailRepository bloodDonationDetailRepository;
    @Autowired
    private BloodTestStoreService bloodTestStoreService;


    @Override
    public BloodInventoryDto add(BloodInventoryDto bloodInventoryDto) {
        if (ObjectUtils.isEmpty(bloodInventoryDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Inventory"});
        }
        if (!ObjectUtils.isEmpty(bloodInventoryDto.getInventoryCode())) {
            throw new BloodBankException(NEW_ENTITY_WITH_ID_CODE,
                    new Object[]{"BloodInventory", bloodInventoryDto.getInventoryCode()});
        }
        BloodBankBranch bloodBankBranch =
                bloodBankBranchService.getBloodBankBranchModelByBranchCode(bloodInventoryDto.getBloodBankBranchCode());
        if (ObjectUtils.isEmpty(bloodBankBranch)){
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodBankBranch ", bloodInventoryDto.getBloodBankBranchCode()});
        }
        BloodDonationDetail donationDetail = bloodDonationDetailRepository.findByDonationUniqueId(bloodInventoryDto.getDonationUniqueId());
        if (ObjectUtils.isEmpty(donationDetail)){
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"donationDetail ", bloodInventoryDto.getDonationUniqueId()});
        }

        BloodInventory bloodInventory = new BloodInventory();
        BloodTestStore bloodTestStore = bloodTestStoreService.getBloodTestStoreByTestId(bloodInventoryDto.getTestId());
        if (!ObjectUtils.isEmpty(bloodTestStore)){
            bloodInventory.setBloodTestStore(bloodTestStore);
        }

        BeanUtils.copyProperties(bloodInventoryDto, bloodInventory);
        bloodInventory.setBloodBankBranch(bloodBankBranch);
        bloodInventory.setBloodDonationDetail(donationDetail);
        bloodInventory.setActive(false);
        bloodInventory.setExpiresOn(getExpiryDate(bloodInventoryDto.getBloodSubType()));
        bloodInventory = bloodInventoryRepository.save(bloodInventory);
        bloodInventory.setInventoryCode(BLOOD_INVENTORY_PREFIX+bloodBankBranch.getBranchCode()+bloodInventory.getId());
        bloodInventory = bloodInventoryRepository.save(bloodInventory);
        bloodInventoryDto.setInventoryCode(bloodInventory.getInventoryCode());
        return bloodInventoryDto;
    }

    private Date getExpiryDate(BloodSubType bloodSubType) {
        Date today = new Date();
        Long expiryTime = today.getTime() + (long)24 * 60 * 60 * 1000 * (bloodSubType.getExpiryInDays());
        return new Date(expiryTime);
    }

    @Override
    public void update(BloodInventoryDto bloodInventoryDto) {
        if (ObjectUtils.isEmpty(bloodInventoryDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Inventory"});
        }
        BloodInventory bloodInventory = bloodInventoryRepository.findByInventoryCode(bloodInventoryDto.getInventoryCode());
        if (ObjectUtils.isEmpty(bloodInventory)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodInventory", bloodInventoryDto});
        }
        BloodBankBranch bloodBankBranch =
                bloodBankBranchService.getBloodBankBranchModelByBranchCode(bloodInventoryDto.getBloodBankBranchCode());
        if (ObjectUtils.isEmpty(bloodBankBranch)){
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"BloodBankBranch ", bloodInventoryDto.getBloodBankBranchCode()});
        }

        BloodDonationDetail donationDetail = bloodDonationDetailRepository.findByDonationUniqueId(bloodInventoryDto.getDonationUniqueId());
        if (ObjectUtils.isEmpty(donationDetail)){
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"donationDetail ", bloodInventoryDto.getDonationUniqueId()});
        }
        BloodTestStore bloodTestStore = bloodTestStoreService.getBloodTestStoreByTestId(bloodInventoryDto.getTestId());
        if (!ObjectUtils.isEmpty(bloodTestStore)){
            bloodInventory.setBloodTestStore(bloodTestStore);
        }
        bloodInventory.setBloodDonationDetail(donationDetail);
        bloodInventory.setBloodBankBranch(bloodBankBranch);
        bloodInventory.setActive(false);
        bloodInventory.setBloodSubType(bloodInventoryDto.getBloodSubType());
        bloodInventory.setBloodGroup(bloodInventoryDto.getBloodGroup());
        bloodInventory.setQuantityInMl(bloodInventoryDto.getQuantityInMl());
        bloodInventoryRepository.save(bloodInventory);
    }

    @Override
    public BloodInventoryDto getBlood(BloodGroup bloodGroup, BloodSubType bloodSubType, Integer quantity) {
        BloodInventory bloodInventory = bloodInventoryRepository.
                findOneByActiveTrueAndBloodGroupAndBloodSubTypeAndQuantityInMlGreaterThanEqualOrderByQuantityInMlAsc(bloodGroup, bloodSubType, quantity);
        BloodInventoryDto bloodInventoryDto = null;
        if (!ObjectUtils.isEmpty(bloodInventory)){
            bloodInventoryDto = new BloodInventoryDto();
            BeanUtils.copyProperties(bloodInventory, bloodInventoryDto);
            bloodInventoryDto.setQuantityInMl(quantity);
            bloodInventory.setQuantityInMl(bloodInventory.getQuantityInMl() - quantity);
            bloodInventoryDto.setBloodBankBranchCode(bloodInventory.getBloodBankBranch().getBranchCode());
            bloodInventoryRepository.save(bloodInventory);
        }
        return bloodInventoryDto;
    }

    @Override
    public BloodInventory getBloodInventoryByDonationUniqueCode(String donationUniqueId) {
        return bloodInventoryRepository.findByBloodDonationDetailDonationUniqueId(donationUniqueId);
    }

    @Override
    public BloodInventory getBloodInventoryByInventoryCode(String inventoryCode) {
        return bloodInventoryRepository.findByInventoryCode(inventoryCode);
    }

    @Override
    public boolean isBloodAvailable(BloodGroup bloodGroup, BloodSubType bloodSubType, Integer quantity) {
        BloodInventory bloodInventory = bloodInventoryRepository.
                findOneByActiveTrueAndBloodGroupAndBloodSubTypeAndQuantityInMlGreaterThanEqualOrderByQuantityInMlAsc(bloodGroup, bloodSubType, quantity);
        return bloodInventory != null ;
    }

    @Override
    public void markInventoryAsActive(String testId) {
        BloodInventory bloodInventory = bloodInventoryRepository.findByBloodTestStoreTestId(testId);
        bloodInventory.setActive(true);
        bloodInventoryRepository.save(bloodInventory);
    }
}
