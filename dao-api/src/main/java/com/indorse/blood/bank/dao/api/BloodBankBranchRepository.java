package com.indorse.blood.bank.dao.api;

import com.indorse.blood.bank.model.BloodBankBranch;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodBankBranchRepository extends CrudRepository<BloodBankBranch, Long> {

    /**
     * Get BloodBank Branch by branch Code
     * @param branchCode
     * @return
     */
    BloodBankBranch findByBranchCode(String branchCode);

    List<BloodBankBranch> findAllByBloodBankName(String bankName);
}
