package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodDonationDetailRepository;
import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.model.BloodDonationDetail;
import com.indorse.blood.bank.model.BloodInventory;
import com.indorse.blood.bank.model.Member;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.*;
import com.indorse.blood.bank.rest.web.model.constant.StatsPeriod;
import com.indorse.blood.bank.service.api.*;
import com.indorse.blood.bank.service.impl.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

import static com.indorse.blood.bank.model.constant.ErrorCode.*;

@Service
public class BloodDonationDetailServiceImpl implements BloodDonationDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BloodDonationDetailServiceImpl.class);
    private static final String BLOOD_DONATION_PREFIX = "BDD";

    @Autowired
    private BloodDonationDetailRepository bloodDonationDetailRepository;

    @Autowired
    private BloodTestStoreService bloodTestStoreService;

    @Autowired
    private BloodInventoryService bloodInventoryService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BloodBankBranchService bloodBankBranchService;


    @Override
    public BloodDonationDetailDto save(BloodDonationDetailDto donationDetailDto) {
        if (ObjectUtils.isEmpty(donationDetailDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Donation Details"});
        }
        if (!ObjectUtils.isEmpty(donationDetailDto.getDonationUniqueId())) {
            throw new BloodBankException(NEW_ENTITY_WITH_ID_CODE, new Object[]{"Blood Donation Detail", "DonationId", donationDetailDto.getDonationUniqueId()});
        }
        BloodDonationDetail donationDetail = new BloodDonationDetail();
        BeanUtils.copyProperties(donationDetailDto, donationDetail);
        if (ObjectUtils.isEmpty(donationDetail.getDonatedOn())) {
            donationDetail.setDonatedOn(new Date());
        }
        BloodBankBranch bloodBankBranch = bloodBankBranchService.getBloodBankBranchModelByBranchCode(donationDetailDto.getBloodBankBranchCode());
        if (ObjectUtils.isEmpty(bloodBankBranch)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood Bank Branch", donationDetailDto.getBloodBankBranchCode()});
        }
        donationDetail.setBloodBankBranch(bloodBankBranch);
        Member member = memberService.getByMemberId(donationDetailDto.getMemberId());
        if (ObjectUtils.isEmpty(member)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood Donor", donationDetailDto.getMemberId()});
        }
        donationDetail.setMember(member);

        donationDetail = bloodDonationDetailRepository.save(donationDetail);
        donationDetail.setDonationUniqueId(BLOOD_DONATION_PREFIX + donationDetail.getId());
        donationDetailDto.setDonationUniqueId(donationDetail.getDonationUniqueId());

        // Add blood to inventory
        BloodInventoryDto bloodInventoryDto = new BloodInventoryDto();
        bloodInventoryDto.setBloodBankBranchCode(donationDetailDto.getBloodBankBranchCode());
        bloodInventoryDto.setBloodGroup(donationDetailDto.getBloodGroup());
        bloodInventoryDto.setBloodSubType(donationDetailDto.getBloodSubType());
        bloodInventoryDto.setQuantityInMl(donationDetailDto.getQuantityInMl());
        bloodInventoryDto.setDonationUniqueId(donationDetail.getDonationUniqueId());

        bloodInventoryDto = bloodInventoryService.add(bloodInventoryDto);
        donationDetailDto.setInventoryCode(bloodInventoryDto.getInventoryCode());

        donationDetail = bloodDonationDetailRepository.save(donationDetail);

        return donationDetailDto;
    }

    @Override
    public void update(BloodDonationDetailDto donationDetailDto) {
        if (ObjectUtils.isEmpty(donationDetailDto)) {
            throw new BloodBankException(CRUD_EMPTY_ENTITY_ERROR, new Object[]{"Blood Donation Details"});
        }
        if (ObjectUtils.isEmpty(donationDetailDto.getDonationUniqueId())) {
            throw new BloodBankException(NEW_ENTITY_WITH_ID_CODE, new Object[]{"Blood Donation Detail", donationDetailDto.getDonationUniqueId()});
        }
        BloodDonationDetail donationDetail = bloodDonationDetailRepository.findByDonationUniqueId(donationDetailDto.getDonationUniqueId());
        BeanUtils.copyProperties(donationDetailDto, donationDetail);
        if (ObjectUtils.isEmpty(donationDetail)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND,
                    new Object[]{"Blood donation detail", "donationUniqueId Id = " + donationDetailDto.getDonationUniqueId()});
        }
        Member member = memberService.getByMemberId(donationDetailDto.getMemberId());
        if (ObjectUtils.isEmpty(member)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood Donor", donationDetailDto.getMemberId()});
        }
        donationDetail.setMember(member);
        BloodBankBranch bloodBankBranch = bloodBankBranchService.getBloodBankBranchModelByBranchCode(donationDetailDto.getBloodBankBranchCode());
        if (ObjectUtils.isEmpty(bloodBankBranch)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood Bank Branch", donationDetailDto.getBloodBankBranchCode()});
        }
        donationDetail.setBloodBankBranch(bloodBankBranch);

        donationDetail.setBloodGroup(donationDetailDto.getBloodGroup());
        donationDetail.setBloodSubType(donationDetailDto.getBloodSubType());
        donationDetail.setQuantityInMl(donationDetail.getQuantityInMl());

        BloodInventory bloodInventory = bloodInventoryService.getBloodInventoryByDonationUniqueCode(donationDetail.getDonationUniqueId());
        BloodInventoryDto bloodInventoryDto = new BloodInventoryDto();
        bloodInventoryDto.setQuantityInMl(donationDetailDto.getQuantityInMl());
        bloodInventoryDto.setBloodGroup(donationDetailDto.getBloodGroup());
        bloodInventoryDto.setBloodSubType(donationDetailDto.getBloodSubType());
        bloodInventoryDto.setInventoryCode(bloodInventory.getInventoryCode());
        bloodInventoryService.update(bloodInventoryDto);
        donationDetail = bloodDonationDetailRepository.save(donationDetail);
    }

    @Override
    public List<BloodDonationDetailDto> getDetailsByMemberId(String memberId) {
        List<BloodDonationDetail> donationDetails = bloodDonationDetailRepository.findAllByMemberMemberId(memberId);
        List<BloodDonationDetailDto> donationDetailDtoList = new ArrayList<>();
        for (BloodDonationDetail donationDetail : donationDetails) {
            BloodDonationDetailDto bloodDonationDetailDto = new BloodDonationDetailDto();
            BeanUtils.copyProperties(donationDetail, bloodDonationDetailDto);
            donationDetailDtoList.add(bloodDonationDetailDto);
        }
        return donationDetailDtoList;
    }

    @Override
    public void delete(String donationUniqueId) {
        BloodDonationDetail bloodDonationDetail = bloodDonationDetailRepository.findByDonationUniqueId(donationUniqueId);
        if (ObjectUtils.isEmpty(bloodDonationDetail)) {
            throw new BloodBankException(RESOURCE_NOT_FOUND, new Object[]{"Blood donation detail", "donationUniqueId Id = " + donationUniqueId});
        }
        bloodDonationDetail.setMarkForDelete(true);
        bloodDonationDetailRepository.save(bloodDonationDetail);
    }

    @Override
    public List<DonorStatDto> getTopDonorsByPeriod(StatsPeriod period, int month, int year, int limit) {
        Date[] dateRange;
        Calendar calendar = Calendar.getInstance();
        switch (period){
            case MONTH:
                calendar.set(Calendar.MONTH, month-1);
                calendar.set(Calendar.YEAR, year);
                dateRange =  DateUtil.getMonthInterval(calendar.getTime());
                break;
            case YEAR:
                calendar.set(Calendar.YEAR, year);
                dateRange =  DateUtil.getYearInterval(calendar.getTime());
                break;
            default:
                dateRange =  DateUtil.getYearInterval(new Date());
                break;
        }

        List<DonorStatDto> donorStats =
                bloodDonationDetailRepository.getDonorStatsByDateRange(dateRange[0], dateRange[1], new PageRequest(0, limit));
        LOGGER.info(Arrays.toString(donorStats.toArray()));
        return donorStats;
    }


}
