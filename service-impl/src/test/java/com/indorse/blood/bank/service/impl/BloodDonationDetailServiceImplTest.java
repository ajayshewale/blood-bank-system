package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodDonationDetailRepository;
import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.model.BloodDonationDetail;
import com.indorse.blood.bank.model.BloodInventory;
import com.indorse.blood.bank.model.Member;
import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodDonationDetailDto;
import com.indorse.blood.bank.rest.web.model.BloodInventoryDto;
import com.indorse.blood.bank.rest.web.model.DonorStatDto;
import com.indorse.blood.bank.rest.web.model.constant.StatsPeriod;
import com.indorse.blood.bank.service.api.BloodBankBranchService;
import com.indorse.blood.bank.service.api.BloodInventoryService;
import com.indorse.blood.bank.service.api.BloodTestStoreService;
import com.indorse.blood.bank.service.api.MemberService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BloodDonationDetailServiceImplTest {

    private static final String BRANCH_CODE = "BB1BBR1";
    private static final String MEMBER_ID = "memberId";
    private static final String MEMBER_NAME = "memberName";
    private static final BloodGroup BLOOD_GROUP = BloodGroup.AP;
    private static final String INVENTORY_CODE = "BI1";
    private static final String DONATION_ID = "donationId";

    @InjectMocks
    private BloodDonationDetailServiceImpl bloodDonationDetailService;

    @Mock
    private BloodDonationDetailRepository bloodDonationDetailRepository;
    @Mock
    private BloodTestStoreService bloodTestStoreService;
    @Mock
    private BloodInventoryService bloodInventoryService;
    @Mock
    private MemberService memberService;
    @Mock
    private BloodBankBranchService bloodBankBranchService;

    @Captor
    private ArgumentCaptor<BloodDonationDetail> donationDetailArgumentCaptor;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(bloodTestStoreService);
        verifyNoMoreInteractions(bloodInventoryService);
        verifyNoMoreInteractions(memberService);
        verifyNoMoreInteractions(bloodBankBranchService);
    }

    @Test
    public void test_add_nullDto_fail(){
        try {
            bloodDonationDetailService.save(null);
        } catch (BloodBankException e) {
            assertEquals(e.getErrorCode(), CRUD_EMPTY_ENTITY_ERROR);
        }
    }

    @Test
    public void test_addDonationDetail_success(){
        BloodDonationDetailDto bloodDonationDetailDto = new BloodDonationDetailDto();
        bloodDonationDetailDto.setBloodBankBranchCode(BRANCH_CODE);
        bloodDonationDetailDto.setBloodGroup(BLOOD_GROUP);
        bloodDonationDetailDto.setMemberId(MEMBER_ID);


        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        when(bloodBankBranchService.getBloodBankBranchModelByBranchCode(BRANCH_CODE)).thenReturn(bloodBankBranch);

        Member member = new Member();
        member.setMemberId(MEMBER_ID);
        when(memberService.getByMemberId(MEMBER_ID)).thenReturn(member);

        BloodDonationDetail bloodDonationDetail = new BloodDonationDetail();
        when(bloodDonationDetailRepository.save(any())).thenReturn(bloodDonationDetail);
        BloodInventoryDto bloodInventoryDto = new BloodInventoryDto();
        bloodInventoryDto.setInventoryCode(INVENTORY_CODE);
        when(bloodInventoryService.add(any())).thenReturn(bloodInventoryDto);
        BloodDonationDetailDto saveDto = bloodDonationDetailService.save(bloodDonationDetailDto);

        verify(memberService).getByMemberId(MEMBER_ID);
        verify(bloodBankBranchService).getBloodBankBranchModelByBranchCode(BRANCH_CODE);
        verify(bloodInventoryService).add(any());
        verify(bloodDonationDetailRepository,times(2)).save(donationDetailArgumentCaptor.capture());
        assertEquals(donationDetailArgumentCaptor.getAllValues().get(0).getBloodGroup(), BLOOD_GROUP);
    }


    @Test
    public void test_updateDonationDetail_success(){
        BloodDonationDetailDto bloodDonationDetailDto = new BloodDonationDetailDto();
        bloodDonationDetailDto.setBloodBankBranchCode(BRANCH_CODE);
        bloodDonationDetailDto.setBloodGroup(BLOOD_GROUP);
        bloodDonationDetailDto.setMemberId(MEMBER_ID);
        bloodDonationDetailDto.setDonationUniqueId(DONATION_ID);


        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        when(bloodBankBranchService.getBloodBankBranchModelByBranchCode(BRANCH_CODE)).thenReturn(bloodBankBranch);

        Member member = new Member();
        member.setMemberId(MEMBER_ID);
        when(memberService.getByMemberId(MEMBER_ID)).thenReturn(member);

        BloodDonationDetail bloodDonationDetail = new BloodDonationDetail();
        bloodDonationDetail.setBloodGroup(BLOOD_GROUP);
        when(bloodDonationDetailRepository.findByDonationUniqueId(DONATION_ID)).thenReturn(bloodDonationDetail);
        BloodInventory bloodInventory = new BloodInventory();
        bloodInventory.setInventoryCode(INVENTORY_CODE);
        when(bloodInventoryService.getBloodInventoryByDonationUniqueCode(DONATION_ID)).thenReturn(bloodInventory);
        bloodDonationDetailService.update(bloodDonationDetailDto);

        verify(memberService).getByMemberId(MEMBER_ID);
        verify(bloodDonationDetailRepository).findByDonationUniqueId(DONATION_ID);
        verify(bloodBankBranchService).getBloodBankBranchModelByBranchCode(BRANCH_CODE);
        verify(bloodInventoryService).getBloodInventoryByDonationUniqueCode(DONATION_ID);
        verify(bloodInventoryService).update(any());
        verify(bloodDonationDetailRepository).save(donationDetailArgumentCaptor.capture());
        assertEquals(donationDetailArgumentCaptor.getValue().getBloodGroup(), BLOOD_GROUP);
    }

    @Test
    public void test_getDetailsByMemberId_success(){
        BloodDonationDetail bloodDonationDetail = new BloodDonationDetail();
        bloodDonationDetail.setBloodGroup(BLOOD_GROUP);
        bloodDonationDetail.setQuantityInMl(10);
        List<BloodDonationDetail> bloodDonationDetails = Arrays.asList(bloodDonationDetail);
        when(bloodDonationDetailRepository.findAllByMemberMemberId(MEMBER_ID)).thenReturn(bloodDonationDetails);
        List<BloodDonationDetailDto> bloodDonationDetailDtos = bloodDonationDetailService.getDetailsByMemberId(MEMBER_ID);
        verify(bloodDonationDetailRepository).findAllByMemberMemberId(MEMBER_ID);
        assertEquals(BLOOD_GROUP,bloodDonationDetailDtos.get(0).getBloodGroup());
    }

    @Test
    public void test_delete_success(){
        BloodDonationDetail bloodDonationDetail = new BloodDonationDetail();
        bloodDonationDetail.setBloodGroup(BLOOD_GROUP);
        when(bloodDonationDetailRepository.findByDonationUniqueId(DONATION_ID)).thenReturn(bloodDonationDetail);
        bloodDonationDetailService.delete(DONATION_ID);
        when(bloodDonationDetailRepository.findByDonationUniqueId(DONATION_ID)).thenReturn(bloodDonationDetail);
        verify(bloodDonationDetailRepository).save(donationDetailArgumentCaptor.capture());
        assertTrue(donationDetailArgumentCaptor.getValue().getMarkForDelete());
    }

    @Test
    public void test_getTopDonorsByPeriod_success(){
        List<DonorStatDto> donorStatDtos = Arrays.asList(new DonorStatDto(10l, MEMBER_NAME, MEMBER_ID),
                new DonorStatDto(9l, MEMBER_NAME+1, MEMBER_ID+1));
        when(bloodDonationDetailRepository.getDonorStatsByDateRange(any(Date.class), any(Date.class), any())).thenReturn(donorStatDtos);
        List<DonorStatDto> result = bloodDonationDetailService.getTopDonorsByPeriod(StatsPeriod.MONTH, 1, 2019, 3);
        assertEquals(Long.valueOf(10), result.get(0).getDonatedBlood());
        verify(bloodDonationDetailRepository).getDonorStatsByDateRange(any(Date.class), any(Date.class), eq(new PageRequest(0,3)));
    }
}