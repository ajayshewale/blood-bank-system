package com.indorse.blood.bank.dao.api;

import com.indorse.blood.bank.model.BloodTestStore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodTestStoreRepository extends CrudRepository<BloodTestStore, Long> {

    BloodTestStore findByTestId(String testId);
}
