package com.indorse.blood.bank.service.api;

import com.indorse.blood.bank.model.BloodBank;
import com.indorse.blood.bank.rest.web.model.BloodBankDto;
import org.springframework.stereotype.Service;

@Service
public interface BloodBankService {

    /**
     * Save or add/register new @link{BloodBank}
     * @param bloodBankDto
     * @return
     */
    BloodBankDto save(BloodBankDto bloodBankDto);

    /**
     * Update existing @link{BloodBank} details
     * @param bloodBankDto
     */
    void update(BloodBankDto bloodBankDto);

    /**
     * Get @link{BloodBank} detail by name
     * @param name
     * @return
     */
    BloodBankDto getByName(String name);

    /**
     * Delete Bank by bankName
     * @param bloodBankName
     * @param bloodBankCode
     */
    void deleteByNameOrBankCode(String bloodBankName, String bloodBankCode);

    /**
     * Get @link{BloodBank} detail by name
     * @param name
     * @return
     */
    BloodBank getBloodBankModelByName(String name);

}
