package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodBankRepository;
import com.indorse.blood.bank.model.BloodBank;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodBankDto;
import com.indorse.blood.bank.service.api.BloodBankBranchService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static com.indorse.blood.bank.service.impl.BloodBankServiceImpl.BLOOD_BANK_PREFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BloodBankServiceImplTest {

    public static final String BLOOD_BANK_NAME = "bloodBankName";

    @InjectMocks
    private BloodBankServiceImpl bloodBankService;

    @Mock
    private BloodBankRepository bloodBankRepository;
    @Mock
    private BloodBankBranchService bloodBankBranchService;

    @Captor
    private ArgumentCaptor<BloodBank> bloodBankArgumentCaptor;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(bloodBankBranchService);
        verifyNoMoreInteractions(bloodBankRepository);
    }

    @Test
    public void test_saveBank_nullDto_fail() {
        try {
            bloodBankService.save(null);
        } catch (BloodBankException e) {
            assertEquals(CRUD_EMPTY_ENTITY_ERROR, e.getErrorCode());
        }
    }

    @Test
    public void test_saveBank_success() {
        BloodBankDto bloodBankDto = new BloodBankDto();
        bloodBankDto.setName(BLOOD_BANK_NAME);
        BloodBank bloodBank = new BloodBank();
        bloodBank.setId(1l);
        bloodBank.setName(BLOOD_BANK_NAME);
        when(bloodBankRepository.save(any())).thenReturn(bloodBank);
        BloodBankDto saveBankDto = bloodBankService.save(bloodBankDto);
        assertEquals(BLOOD_BANK_PREFIX+1, bloodBankDto.getBankCode());
        verify(bloodBankRepository, times(2)).save(any());
    }

    @Test
    public void test_updateBank_success() {
        BloodBankDto bloodBankDto = new BloodBankDto();
        bloodBankDto.setName(BLOOD_BANK_NAME);
        bloodBankDto.setBankCode(BLOOD_BANK_PREFIX);
        BloodBank bloodBank = new BloodBank();
        bloodBank.setId(1l);
        bloodBank.setName(BLOOD_BANK_NAME);
        when(bloodBankRepository.findByBankCode(anyString())).thenReturn(bloodBank);
        bloodBankService.update(bloodBankDto);
        verify(bloodBankRepository).save(any());
        verify(bloodBankRepository).findByBankCode(BLOOD_BANK_PREFIX);
    }


    @Test
    public void test_getBank_success(){
        BloodBank bloodBank = new BloodBank();
        bloodBank.setId(1l);
        bloodBank.setName(BLOOD_BANK_NAME);
        bloodBank.setBankCode(BLOOD_BANK_PREFIX);
        when(bloodBankRepository.findByName(anyString())).thenReturn(bloodBank);
        BloodBankDto saveBankDto = bloodBankService.getByName(BLOOD_BANK_NAME);
        assertEquals(BLOOD_BANK_PREFIX, saveBankDto.getBankCode());
        verify(bloodBankRepository).findByName(eq(BLOOD_BANK_NAME));
    }

    @Test
    public void test_deleteBank_success(){
        BloodBank bloodBank = new BloodBank();
        bloodBank.setId(1l);
        bloodBank.setName(BLOOD_BANK_NAME);
        bloodBank.setBankCode(BLOOD_BANK_PREFIX);
        when(bloodBankRepository.findByBankCodeOrName(anyString(),anyString())).thenReturn(bloodBank);
        bloodBankService.deleteByNameOrBankCode(BLOOD_BANK_NAME, BLOOD_BANK_PREFIX);
        verify(bloodBankRepository).findByBankCodeOrName(eq(BLOOD_BANK_PREFIX),eq(BLOOD_BANK_NAME));
        verify(bloodBankRepository).save(bloodBankArgumentCaptor.capture());
        verify(bloodBankBranchService).deleteAllBloodBankBranches(eq(BLOOD_BANK_NAME));
        assertTrue(bloodBankArgumentCaptor.getValue().getMarkForDelete());
    }
}