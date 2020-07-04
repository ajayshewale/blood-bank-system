package com.indorse.blood.bank.dao.api;

import com.indorse.blood.bank.model.BloodBank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodBankRepository extends CrudRepository<BloodBank, Long> {

    /**
     * Get blood bank by bank name
     * @param name
     * @return
     */
    BloodBank findByName(String name);

    /**
     * Get blood bank by bank code
     * @param bankCode
     * @return
     */
    BloodBank findByBankCode(String bankCode);

    /**
     * Get blood bank by bank code or bank name
     * @param bloodBankCode
     * @param name
     * @return
     */
    BloodBank findByBankCodeOrName(String bloodBankCode, String name);
}
