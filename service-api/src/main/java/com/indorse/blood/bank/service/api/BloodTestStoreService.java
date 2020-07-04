package com.indorse.blood.bank.service.api;

import com.indorse.blood.bank.model.BloodTestStore;
import com.indorse.blood.bank.rest.web.model.BloodTestStoreDto;
import org.springframework.stereotype.Service;

@Service
public interface BloodTestStoreService {

    /**
     * Add bloodTest after blood donation
     * @param bloodTestStoreDto
     * @return
     */
    BloodTestStoreDto add(BloodTestStoreDto bloodTestStoreDto);

    /**
     * Update existing bloodTest
     * @param bloodTestStoreDto
     */
    void update(BloodTestStoreDto bloodTestStoreDto);

    /**
     * Get BloodTest detail by testId
     * @param testId
     * @return
     */
    BloodTestStoreDto getByTestId(String testId);

    /**
     * Get test store for a inventory block
     * @param inventoryCode
     * @return
     */
    BloodTestStoreDto getByInventoryCode(String inventoryCode);

    /**
     * Get BloodTest detail by testId
     * @param testId
     * @return
     */
    BloodTestStore getBloodTestStoreByTestId(String testId);


    /**
     * update blood test result by testId
     * @param testId
     * @param passed
     */
    void updateTestResult(String testId, boolean passed, String info);
}
