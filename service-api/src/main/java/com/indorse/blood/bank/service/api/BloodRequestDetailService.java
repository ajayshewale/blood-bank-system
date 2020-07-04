package com.indorse.blood.bank.service.api;

import com.indorse.blood.bank.rest.web.model.BloodRequestDetailDto;
import org.springframework.stereotype.Service;

@Service
public interface BloodRequestDetailService {

    /**
     * save blood request
     * @param bloodRequestDetailDto
     */
    BloodRequestDetailDto add(BloodRequestDetailDto bloodRequestDetailDto);

    /**
     * update blood request
     * @param bloodRequestDetailDto
     */
    void update(BloodRequestDetailDto bloodRequestDetailDto);

    /**
     * Get blood request by ID
     * @param bloodRequestId
     * @return
     */
    BloodRequestDetailDto getBloodRequestDetail(String bloodRequestId);

    /**
     * Delete bloodRequestDetail by id
     * @param bloodRequestId
     */
    void deleteBloodRequestDetailById(String bloodRequestId);

    /**
     * Return requested blood if available or return null
     * @param bloodRequestDetailDto
     * @return
     */
    BloodRequestDetailDto requestBlood(BloodRequestDetailDto bloodRequestDetailDto);
}
