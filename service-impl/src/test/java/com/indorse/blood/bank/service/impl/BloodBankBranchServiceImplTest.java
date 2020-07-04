package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodBankBranchRepository;
import com.indorse.blood.bank.model.BloodBank;
import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodBankBranchDto;
import com.indorse.blood.bank.service.api.BloodBankService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.indorse.blood.bank.model.constant.ErrorCode.BLOOD_BANK_NOT_EXIST;
import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static com.indorse.blood.bank.service.impl.BloodBankBranchServiceImpl.BRANCH_CODE_PREFIX;
import static com.indorse.blood.bank.service.impl.BloodBankServiceImpl.BLOOD_BANK_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BloodBankBranchServiceImplTest {

    private static final String  BLOOD_BANK_NAME = "bloodBankName";
    private static final Long  BLOOD_BANK_ID = 1l;
    private static final String  BLOOD_BANK_CODE = BLOOD_BANK_PREFIX+1;
    private static final String  BLOOD_BANK_BRANCH_NAME = "bloodBankBranchName";
    private static final Long  BLOOD_BANK_BRANCH_ID = 1l;
    private static final String  BLOOD_BANK_BRANCH_CODE = "BB1BBR1";

    @InjectMocks
    private BloodBankBranchServiceImpl bloodBankBranchService;
    @Mock
    private BloodBankBranchRepository bloodBankBranchRepository;
    @Mock
    private BloodBankService bloodBankService;

    @Captor
    private ArgumentCaptor<BloodBankBranch> bloodBankBranchArgumentCaptor;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(bloodBankService);
    }

    @Test
    public void test_addBloodBankBranch_nullDto_fail() {
        try {
            bloodBankBranchService.add(null);
        } catch (BloodBankException e) {
            assertEquals(e.getErrorCode(), CRUD_EMPTY_ENTITY_ERROR);
        }
    }

    @Test
    public void test_addBloodBankBranch_bankNotExists_fail() {
        BloodBankBranchDto bloodBankBranchDto = new BloodBankBranchDto();
        bloodBankBranchDto.setBloodBankName(BLOOD_BANK_NAME);
        when(bloodBankService.getBloodBankModelByName(BLOOD_BANK_NAME)).thenReturn(null);
        try {
            bloodBankBranchService.add(bloodBankBranchDto);
        } catch (BloodBankException e) {
            assertEquals(e.getErrorCode(), BLOOD_BANK_NOT_EXIST);
        }
        verify(bloodBankService).getBloodBankModelByName(BLOOD_BANK_NAME);
    }

    @Test
    public void test_addBloodBankBranch_success() {
        BloodBankBranchDto bloodBankBranchDto = new BloodBankBranchDto();
        bloodBankBranchDto.setBloodBankName(BLOOD_BANK_NAME);
        BloodBank bloodBank = new BloodBank();
        bloodBank.setName(BLOOD_BANK_NAME);
        bloodBank.setId(BLOOD_BANK_ID);
        bloodBank.setBankCode(BLOOD_BANK_CODE);
        when(bloodBankService.getBloodBankModelByName(BLOOD_BANK_NAME)).thenReturn(bloodBank);
        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBloodBank(bloodBank);
        bloodBankBranch.setId(BLOOD_BANK_BRANCH_ID);
        when(bloodBankBranchRepository.save(bloodBankBranch)).thenReturn(bloodBankBranch);
        BloodBankBranchDto savedBranchDto = bloodBankBranchService.add(bloodBankBranchDto);
        assertEquals(BLOOD_BANK_CODE+BRANCH_CODE_PREFIX+1, savedBranchDto.getBranchCode());
        verify(bloodBankService).getBloodBankModelByName(BLOOD_BANK_NAME);
        verify(bloodBankBranchRepository).save(bloodBankBranch);
    }


    @Test
    public void test_updateBloodBankBranch_success() {
        BloodBankBranchDto bloodBankBranchDto = new BloodBankBranchDto();
        bloodBankBranchDto.setBloodBankName(BLOOD_BANK_NAME);
        bloodBankBranchDto.setBranchCode(BLOOD_BANK_BRANCH_CODE);
        bloodBankBranchDto.setBranchName(BLOOD_BANK_BRANCH_NAME+1);

        BloodBankBranch existingBloodBankBranch = new BloodBankBranch();
        existingBloodBankBranch.setBranchCode(BLOOD_BANK_BRANCH_CODE);
        existingBloodBankBranch.setId(BLOOD_BANK_BRANCH_ID);
        when(bloodBankBranchRepository.findByBranchCode(BLOOD_BANK_BRANCH_CODE)).thenReturn(existingBloodBankBranch);

        BloodBank bloodBank = new BloodBank();
        bloodBank.setName(BLOOD_BANK_NAME);
        bloodBank.setId(BLOOD_BANK_ID);
        bloodBank.setBankCode(BLOOD_BANK_CODE);
        when(bloodBankService.getBloodBankModelByName(BLOOD_BANK_NAME)).thenReturn(bloodBank);

        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBloodBank(bloodBank);
        bloodBankBranch.setId(BLOOD_BANK_BRANCH_ID);
        when(bloodBankBranchRepository.save(bloodBankBranch)).thenReturn(bloodBankBranch);
        bloodBankBranchService.update(bloodBankBranchDto);
        verify(bloodBankService).getBloodBankModelByName(BLOOD_BANK_NAME);
        verify(bloodBankBranchRepository).findByBranchCode(BLOOD_BANK_BRANCH_CODE);
        verify(bloodBankBranchRepository).save(bloodBankBranchArgumentCaptor.capture());
        assertEquals(BLOOD_BANK_BRANCH_NAME+1, bloodBankBranchArgumentCaptor.getValue().getBranchName());
    }

    @Test
    public void test_getBloodBankBranch_success() {
        BloodBankBranch existingBloodBankBranch = new BloodBankBranch();
        existingBloodBankBranch.setBranchCode(BLOOD_BANK_BRANCH_CODE);
        existingBloodBankBranch.setId(BLOOD_BANK_BRANCH_ID);
        existingBloodBankBranch.setBranchName(BLOOD_BANK_BRANCH_NAME);
        when(bloodBankBranchRepository.findByBranchCode(BLOOD_BANK_BRANCH_CODE)).thenReturn(existingBloodBankBranch);
        BloodBankBranchDto bloodBankBranchDto = bloodBankBranchService.getByBranchCode(BLOOD_BANK_BRANCH_CODE);
        assertEquals(BLOOD_BANK_BRANCH_NAME,bloodBankBranchDto.getBranchName());
        verify(bloodBankBranchRepository).findByBranchCode(BLOOD_BANK_BRANCH_CODE);
    }

    @Test
    public void test_deleteBloodBankBranch_success() {
        BloodBankBranch existingBloodBankBranch = new BloodBankBranch();
        existingBloodBankBranch.setBranchCode(BLOOD_BANK_BRANCH_CODE);
        existingBloodBankBranch.setId(BLOOD_BANK_BRANCH_ID);
        existingBloodBankBranch.setBranchName(BLOOD_BANK_BRANCH_NAME);
        when(bloodBankBranchRepository.findByBranchCode(BLOOD_BANK_BRANCH_CODE)).thenReturn(existingBloodBankBranch);
        bloodBankBranchService.deleteByBranchCode(BLOOD_BANK_BRANCH_CODE);
        verify(bloodBankBranchRepository).save(bloodBankBranchArgumentCaptor.capture());
        assertTrue(bloodBankBranchArgumentCaptor.getValue().getMarkForDelete());
        verify(bloodBankBranchRepository).findByBranchCode(BLOOD_BANK_BRANCH_CODE);
    }

}