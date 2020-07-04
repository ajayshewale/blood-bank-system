package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.BloodTestStoreRepository;
import com.indorse.blood.bank.model.BloodBankBranch;
import com.indorse.blood.bank.model.BloodDonationDetail;
import com.indorse.blood.bank.model.BloodInventory;
import com.indorse.blood.bank.model.BloodTestStore;
import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.BloodTestStoreDto;
import com.indorse.blood.bank.service.api.BloodDonationDetailService;
import com.indorse.blood.bank.service.api.BloodInventoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BloodTestStoreServiceImplTest {

    private static final String INVENTORY_CODE = "BBI1";
    private static final String BRANCH_CODE = "BB1BBR1";
    private static final String DONATION_ID = "donationId";
    private static final String TEST_ID = "testId";
    private static final BloodGroup BLOOD_GROUP = BloodGroup.AP;


    @InjectMocks
    private BloodTestStoreServiceImpl bloodTestStoreService;

    @Mock
    private BloodTestStoreRepository bloodTestStoreRepository;
    @Mock
    private BloodInventoryService bloodInventoryService;
    @Mock
    private BloodDonationDetailService bloodDonationDetailService;

    @Captor
    private ArgumentCaptor<BloodTestStore> bloodTestStoreArgumentCaptor;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(bloodTestStoreRepository);
        verifyNoMoreInteractions(bloodInventoryService);
        verifyNoMoreInteractions(bloodDonationDetailService);
    }

    @Test
    public void test_add_nullDto_fail() {
        try {
            bloodTestStoreService.add(null);
        } catch (BloodBankException e) {
            assertEquals(CRUD_EMPTY_ENTITY_ERROR, e.getErrorCode());
        }
    }


    @Test
    public void test_add_success() {
        BloodTestStoreDto bloodTestStoreDto = new BloodTestStoreDto();
        bloodTestStoreDto.setInventoryCode(INVENTORY_CODE);

        BloodInventory inventory = new BloodInventory();
        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        inventory.setBloodBankBranch(bloodBankBranch);
        BloodDonationDetail bloodDonationDetail = new BloodDonationDetail();
        bloodDonationDetail.setDonationUniqueId(DONATION_ID);
        inventory.setBloodDonationDetail(bloodDonationDetail);

        when(bloodInventoryService.getBloodInventoryByInventoryCode(INVENTORY_CODE)).thenReturn(inventory);
        when(bloodTestStoreRepository.save(any())).thenReturn(new BloodTestStore());
        bloodTestStoreService.add(bloodTestStoreDto);

        verify(bloodInventoryService).getBloodInventoryByInventoryCode(INVENTORY_CODE);
        verify(bloodInventoryService).update(any());
        verify(bloodTestStoreRepository, times(2)).save(bloodTestStoreArgumentCaptor.capture());
    }

    @Test
    public void test_update_success() {
        BloodTestStoreDto bloodTestStoreDto = new BloodTestStoreDto();
        bloodTestStoreDto.setInventoryCode(INVENTORY_CODE);
        bloodTestStoreDto.setTestId(TEST_ID);
        bloodTestStoreDto.setInfo("test");

        when(bloodTestStoreRepository.save(any())).thenReturn(new BloodTestStore());

        BloodTestStore existingStore = new BloodTestStore();
        existingStore.setTestId(TEST_ID);

        when(bloodTestStoreRepository.findByTestId(TEST_ID)).thenReturn(existingStore);
        bloodTestStoreService.update(bloodTestStoreDto);

        verify(bloodTestStoreRepository).save(bloodTestStoreArgumentCaptor.capture());
        verify(bloodTestStoreRepository).findByTestId(TEST_ID);
        assertEquals("test",bloodTestStoreArgumentCaptor.getValue().getInfo());
    }


    @Test
    public void test_getByTestId_success(){

        BloodTestStore bloodTestStore = new BloodTestStore();
        bloodTestStore.setBloodGroup(BLOOD_GROUP);
        when(bloodTestStoreRepository.findByTestId(TEST_ID)).thenReturn(bloodTestStore);
        BloodTestStoreDto result = bloodTestStoreService.getByTestId(TEST_ID);
        assertEquals(BLOOD_GROUP, result.getBloodGroup());
        verify(bloodTestStoreRepository).findByTestId(TEST_ID);


    }

    @Test
    public void test_getByInventoryCode_success(){
        BloodInventory inventory = new BloodInventory();
        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        inventory.setBloodBankBranch(bloodBankBranch);
        BloodDonationDetail bloodDonationDetail = new BloodDonationDetail();
        bloodDonationDetail.setDonationUniqueId(DONATION_ID);
        inventory.setBloodDonationDetail(bloodDonationDetail);
        BloodTestStore bloodTestStore = new BloodTestStore();
        bloodTestStore.setBloodGroup(BLOOD_GROUP);
        inventory.setBloodTestStore(bloodTestStore);
        when(bloodInventoryService.getBloodInventoryByInventoryCode(INVENTORY_CODE)).thenReturn(inventory);

        BloodTestStoreDto testStoreDto = bloodTestStoreService.getByInventoryCode(INVENTORY_CODE);
        assertEquals(BLOOD_GROUP, testStoreDto.getBloodGroup());
        verify(bloodInventoryService).getBloodInventoryByInventoryCode(INVENTORY_CODE);
    }

    @Test
    public void test_updateTestResult_success(){
        BloodTestStore bloodTestStore = new BloodTestStore();
        bloodTestStore.setBloodGroup(BLOOD_GROUP);

        when(bloodTestStoreRepository.findByTestId(TEST_ID)).thenReturn(bloodTestStore);
        bloodTestStoreService.updateTestResult(TEST_ID, true, "passed");
        verify(bloodTestStoreRepository).findByTestId(TEST_ID);
        verify(bloodInventoryService).markInventoryAsActive(TEST_ID);
        verify(bloodTestStoreRepository).save(any());
    }

}