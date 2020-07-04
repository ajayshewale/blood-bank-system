package com.indorse.blood.bank.dao.api;

import com.indorse.blood.bank.model.BloodInventory;
import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.constant.BloodSubType;
import io.swagger.models.auth.In;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodInventoryRepository extends CrudRepository<BloodInventory, Long> {

    BloodInventory findByBloodDonationDetailDonationUniqueId(String donationUniqueId);

    BloodInventory findByInventoryCode(String inventoryCode);

    BloodInventory findByBloodTestStoreTestId(String testId);

    /**
     * FInd first active inventory by blood group, blood subType and quantity greater or equal
     * @param bloodGroup
     * @param bloodSubType
     * @param quantityInMl
     * @return
     */
    BloodInventory findOneByActiveTrueAndBloodGroupAndBloodSubTypeAndQuantityInMlGreaterThanEqualOrderByQuantityInMlAsc(
            BloodGroup bloodGroup, BloodSubType bloodSubType, Integer quantityInMl);

}
