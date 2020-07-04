package com.indorse.blood.bank.dao.api;

import com.indorse.blood.bank.model.BloodDonationDetail;
import com.indorse.blood.bank.rest.web.model.DonorStatDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BloodDonationDetailRepository extends CrudRepository<BloodDonationDetail, Long> {

    String DONOR_STAT_BY_DATE_RANGE = "SELECT new Donor" ;


    /**
     * Get detail by donationUniqueId
     * @param donationUniqueId
     * @return
     */
    BloodDonationDetail findByDonationUniqueId(String donationUniqueId);

    List<BloodDonationDetail> findAllByMemberMemberId(String memberId);

    @Query("select new com.indorse.blood.bank.rest.web.model.DonorStatDto(sum(bdd.quantityInMl) as donated_blood, bdd.member.firstName as name, bdd.member.memberId as memberId) From BloodDonationDetail bdd where bdd.donatedOn > :from and bdd.donatedOn < :to " +
            " group by bdd.member.memberId, bdd.member.firstName order by donated_blood desc")
    List<DonorStatDto> getDonorStatsByDateRange(Date from, Date to, Pageable pageable);
}
