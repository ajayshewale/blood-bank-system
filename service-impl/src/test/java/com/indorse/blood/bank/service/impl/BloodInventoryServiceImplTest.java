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
import com.indorse.blood.bank.service.api.BloodTestStoreService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static com.indorse.blood.bank.service.impl.BloodInventoryServiceImpl.BLOOD_INVENTORY_PREFIX;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BloodInventoryServiceImplTest {
    private static final String BRANCH_CODE = "BB1BBR1";
    private static final BloodGroup BLOOD_GROUP = BloodGroup.AP;
    private static final String DONATION_ID = "donationId";
    private static final String TEST_ID = "testId";
    private static final String INVENTORY_CODE = "BBI1";


    @InjectMocks
    private BloodInventoryServiceImpl inventoryService;

    @Mock
    private BloodInventoryRepository bloodInventoryRepository;
    @Mock
    private BloodBankBranchService bloodBankBranchService;
    @Mock
    private BloodDonationDetailRepository bloodDonationDetailRepository;
    @Mock
    private BloodTestStoreService bloodTestStoreService;

    @Captor
    private ArgumentCaptor<BloodInventory> inventoryArgumentCaptor;


    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(bloodInventoryRepository);
        verifyNoMoreInteractions(bloodBankBranchService);
        verifyNoMoreInteractions(bloodDonationDetailRepository);
        verifyNoMoreInteractions(bloodTestStoreService);
    }

    @Test
    public void test_add_nullDto_fail(){
        try {
            inventoryService.add(null);
        } catch (BloodBankException e) {
            assertEquals(e.getErrorCode(), CRUD_EMPTY_ENTITY_ERROR);
        }
    }

    @Test
    public void test_add_success(){
        BloodInventoryDto bloodInventoryDto = new BloodInventoryDto();
        bloodInventoryDto.setBloodBankBranchCode(BRANCH_CODE);
        bloodInventoryDto.setDonationUniqueId(DONATION_ID);
        bloodInventoryDto.setTestId(TEST_ID);
        bloodInventoryDto.setBloodSubType(BloodSubType.WHOLE);

        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        when(bloodBankBranchService.getBloodBankBranchModelByBranchCode(BRANCH_CODE)).thenReturn(bloodBankBranch);

        BloodDonationDetail bloodDonationDetail = new BloodDonationDetail();
        bloodDonationDetail.setBloodGroup(BLOOD_GROUP);
        when(bloodDonationDetailRepository.findByDonationUniqueId(DONATION_ID)).thenReturn(bloodDonationDetail);

        BloodTestStore testStore = new BloodTestStore();
        testStore.setTestId(TEST_ID);
        when(bloodTestStoreService.getBloodTestStoreByTestId(TEST_ID)).thenReturn(testStore);

        BloodInventory bloodInventory = new BloodInventory();
        bloodInventory.setId(1l);
        when(bloodInventoryRepository.save(any())).thenReturn(bloodInventory);
        inventoryService.add(bloodInventoryDto);
        verify(bloodBankBranchService).getBloodBankBranchModelByBranchCode(BRANCH_CODE);
        verify(bloodDonationDetailRepository).findByDonationUniqueId(DONATION_ID);
        verify(bloodTestStoreService).getBloodTestStoreByTestId(TEST_ID);
        verify(bloodInventoryRepository, times(2)).save(inventoryArgumentCaptor.capture());
        assertEquals(BLOOD_INVENTORY_PREFIX+BRANCH_CODE+1, inventoryArgumentCaptor.getAllValues().get(1).getInventoryCode());
    }

    @Test
    public void test_update_success(){
        BloodInventoryDto bloodInventoryDto = new BloodInventoryDto();
        bloodInventoryDto.setBloodBankBranchCode(BRANCH_CODE);
        bloodInventoryDto.setDonationUniqueId(DONATION_ID);
        bloodInventoryDto.setTestId(TEST_ID);
        bloodInventoryDto.setBloodSubType(BloodSubType.FFP);
        bloodInventoryDto.setInventoryCode(INVENTORY_CODE);

        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        when(bloodBankBranchService.getBloodBankBranchModelByBranchCode(BRANCH_CODE)).thenReturn(bloodBankBranch);

        BloodDonationDetail bloodDonationDetail = new BloodDonationDetail();
        bloodDonationDetail.setBloodGroup(BLOOD_GROUP);
        when(bloodDonationDetailRepository.findByDonationUniqueId(DONATION_ID)).thenReturn(bloodDonationDetail);

        BloodTestStore testStore = new BloodTestStore();
        testStore.setTestId(TEST_ID);
        when(bloodTestStoreService.getBloodTestStoreByTestId(TEST_ID)).thenReturn(testStore);

        BloodInventory bloodInventory = new BloodInventory();
        bloodInventory.setId(1l);
        when(bloodInventoryRepository.save(any())).thenReturn(bloodInventory);

        when(bloodInventoryRepository.findByInventoryCode(INVENTORY_CODE)).thenReturn(bloodInventory);

        inventoryService.update(bloodInventoryDto);
        verify(bloodBankBranchService).getBloodBankBranchModelByBranchCode(BRANCH_CODE);
        verify(bloodDonationDetailRepository).findByDonationUniqueId(DONATION_ID);
        verify(bloodTestStoreService).getBloodTestStoreByTestId(TEST_ID);
        verify(bloodInventoryRepository).save(inventoryArgumentCaptor.capture());
        verify(bloodInventoryRepository).findByInventoryCode(INVENTORY_CODE);
        assertEquals(BloodSubType.FFP, inventoryArgumentCaptor.getValue().getBloodSubType());
    }

    @Test
    public void test_getBlood_success(){
        BloodInventory bloodInventory = new BloodInventory();
        bloodInventory.setBloodGroup(BLOOD_GROUP);
        bloodInventory.setBloodSubType(BloodSubType.WHOLE);
        bloodInventory.setQuantityInMl(20);
        BloodBankBranch bloodBankBranch = new BloodBankBranch();
        bloodBankBranch.setBranchCode(BRANCH_CODE);
        bloodInventory.setBloodBankBranch(bloodBankBranch);
        when(bloodInventoryRepository.
                findOneByActiveTrueAndBloodGroupAndBloodSubTypeAndQuantityInMlGreaterThanEqualOrderByQuantityInMlAsc(BLOOD_GROUP, BloodSubType.WHOLE, 10)).thenReturn(bloodInventory);
        inventoryService.getBlood(BLOOD_GROUP, BloodSubType.WHOLE, 10);
        verify(bloodInventoryRepository).findOneByActiveTrueAndBloodGroupAndBloodSubTypeAndQuantityInMlGreaterThanEqualOrderByQuantityInMlAsc(eq(BLOOD_GROUP), eq(BloodSubType.WHOLE), eq(10));
        verify(bloodInventoryRepository).save(any());
    }

    @Test
    public void test_markInventoryAsActive_success(){
        when(bloodInventoryRepository.findByBloodTestStoreTestId(TEST_ID)).thenReturn(new BloodInventory());
        inventoryService.markInventoryAsActive(TEST_ID);
        verify(bloodInventoryRepository).findByBloodTestStoreTestId(TEST_ID);
        verify(bloodInventoryRepository).save(inventoryArgumentCaptor.capture());
        assertTrue(inventoryArgumentCaptor.getValue().getActive());
    }
}