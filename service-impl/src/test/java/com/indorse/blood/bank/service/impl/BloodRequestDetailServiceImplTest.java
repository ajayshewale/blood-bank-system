package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodRequestDetailRepository;
import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.model.BloodInventory;
import com.indorse.blood.bank.model.BloodRequestDetail;
import com.indorse.blood.bank.model.Member;
import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodInventoryDto;
import com.indorse.blood.bank.rest.web.model.BloodRequestDetailDto;
import com.indorse.blood.bank.service.api.BloodBankBranchService;
import com.indorse.blood.bank.service.api.BloodInventoryService;
import com.indorse.blood.bank.service.api.MemberService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BloodRequestDetailServiceImplTest {

    private static final String BRANCH_CODE = "BB1BBR1";
    private static final String MEMBER_ID = "memberId";
    private static final String MEMBER_NAME = "memberName";
    private static final BloodGroup BLOOD_GROUP = BloodGroup.AP;
    private static final String INVENTORY_CODE = "BI1";
    private static final String BLOOD_REQUEST_ID = "requestId";

    @InjectMocks
    private BloodRequestDetailServiceImpl bloodRequestDetailService;

    @Mock
    private BloodRequestDetailRepository bloodRequestDetailRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private BloodBankBranchService bloodBankBranchService;
    @Mock
    private BloodInventoryService bloodInventoryService;

    @Captor
    private ArgumentCaptor<BloodRequestDetail> bloodRequestDetailArgumentCaptor;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(bloodRequestDetailRepository);
        verifyNoMoreInteractions(memberService);
        verifyNoMoreInteractions(bloodBankBranchService);
        verifyNoMoreInteractions(bloodInventoryService);
    }

    @Test
    public void test_add_nullDto_fail(){
        try {
            bloodRequestDetailService.add(null);
        } catch (BloodBankException e) {
            assertEquals(e.getErrorCode(), CRUD_EMPTY_ENTITY_ERROR);
        }
    }


    @Test
    public void test_add_success(){
        BloodRequestDetailDto bloodRequestDetailDto = new BloodRequestDetailDto();
        bloodRequestDetailDto.setMemberId(MEMBER_ID);
        bloodRequestDetailDto.setQuantityInMl(10);
        bloodRequestDetailDto.setRequestedFromBranchCode(BRANCH_CODE);

        Member member = new Member();
        member.setMemberId(MEMBER_ID);
        when(memberService.getByMemberId(MEMBER_ID)).thenReturn(member);

        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        when(bloodBankBranchService.getBloodBankBranchModelByBranchCode(BRANCH_CODE)).thenReturn(bloodBankBranch);

        BloodRequestDetail bloodRequestDetail = new BloodRequestDetail();
        bloodRequestDetail.setRequestCompleted(false);
        bloodRequestDetail.setRequestId(BLOOD_REQUEST_ID);
        bloodRequestDetail.setId(1l);
        when(bloodRequestDetailRepository.save(any())).thenReturn(bloodRequestDetail);
        BloodRequestDetailDto result = bloodRequestDetailService.add(bloodRequestDetailDto);

        verify(memberService).getByMemberId(MEMBER_ID);
        verify(bloodBankBranchService).getBloodBankBranchModelByBranchCode(BRANCH_CODE);
        verify(bloodRequestDetailRepository, times(2)).save(bloodRequestDetailArgumentCaptor.capture());
        assertFalse(bloodRequestDetailArgumentCaptor.getAllValues().get(0).isRequestCompleted());
    }

    @Test
    public void test_update_success(){
        BloodRequestDetailDto bloodRequestDetailDto = new BloodRequestDetailDto();
        bloodRequestDetailDto.setMemberId(MEMBER_ID);
        bloodRequestDetailDto.setQuantityInMl(20);
        bloodRequestDetailDto.setRequestedFromBranchCode(BRANCH_CODE);
        bloodRequestDetailDto.setGivenFromBranchCode(BRANCH_CODE);
        bloodRequestDetailDto.setBloodRequestId(BLOOD_REQUEST_ID);

        Member member = new Member();
        member.setMemberId(MEMBER_ID);
        when(memberService.getByMemberId(MEMBER_ID)).thenReturn(member);

        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        when(bloodBankBranchService.getBloodBankBranchModelByBranchCode(BRANCH_CODE)).thenReturn(bloodBankBranch);

        BloodRequestDetail bloodRequestDetail = new BloodRequestDetail();
        bloodRequestDetail.setRequestCompleted(false);
        when(bloodRequestDetailRepository.save(any())).thenReturn(bloodRequestDetail);

        BloodRequestDetail existingBloodRequestDetail = new BloodRequestDetail();
        existingBloodRequestDetail.setQuantityInMl(10);

        when(bloodRequestDetailRepository.findByRequestId(BLOOD_REQUEST_ID)).thenReturn(existingBloodRequestDetail);

        bloodRequestDetailService.update(bloodRequestDetailDto);

        verify(memberService).getByMemberId(MEMBER_ID);
        verify(bloodBankBranchService, times(2)).getBloodBankBranchModelByBranchCode(BRANCH_CODE);
        verify(bloodRequestDetailRepository).save(bloodRequestDetailArgumentCaptor.capture());
        verify(bloodRequestDetailRepository).findByRequestId(BLOOD_REQUEST_ID);
        assertEquals(Integer.valueOf(20), bloodRequestDetailArgumentCaptor.getValue().getQuantityInMl());
    }

    @Test
    public void test_getBloodRequestDetail_success(){
        BloodRequestDetail existingBloodRequestDetail = new BloodRequestDetail();
        existingBloodRequestDetail.setQuantityInMl(10);
        when(bloodRequestDetailRepository.findByRequestId(BLOOD_REQUEST_ID)).thenReturn(existingBloodRequestDetail);
        BloodRequestDetailDto result = bloodRequestDetailService.getBloodRequestDetail(BLOOD_REQUEST_ID);
        assertEquals(Integer.valueOf(10), result.getQuantityInMl());
        verify(bloodRequestDetailRepository).findByRequestId(BLOOD_REQUEST_ID);
    }

    @Test
    public void test_deleteBloodRequestDetail_success(){
        BloodRequestDetail existingBloodRequestDetail = new BloodRequestDetail();
        existingBloodRequestDetail.setQuantityInMl(10);
        when(bloodRequestDetailRepository.findByRequestId(BLOOD_REQUEST_ID)).thenReturn(existingBloodRequestDetail);
        bloodRequestDetailService.deleteBloodRequestDetailById(BLOOD_REQUEST_ID);
        verify(bloodRequestDetailRepository).findByRequestId(BLOOD_REQUEST_ID);
        verify(bloodRequestDetailRepository).save(bloodRequestDetailArgumentCaptor.capture());
        assertTrue(bloodRequestDetailArgumentCaptor.getValue().getMarkForDelete());
    }

}