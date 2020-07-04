package com.indorse.blood.bank.service.api;

import com.indorse.blood.bank.rest.web.model.BloodDonationDetailDto;
import com.indorse.blood.bank.rest.web.model.DonorStatDto;
import com.indorse.blood.bank.rest.web.model.MemberDto;
import com.indorse.blood.bank.rest.web.model.constant.StatsPeriod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BloodDonationDetailService {

    /**
     * Save blood donation detail
     *
     * @param donationDetailDto
     * @return
     */
    BloodDonationDetailDto save(BloodDonationDetailDto donationDetailDto);

    /**
     * update existing blood donation details
     *
     * @param donationDetailDto
     */
    void update(BloodDonationDetailDto donationDetailDto);

    /**
     * Get all donation detail by a member
     *
     * @param memberId
     * @return
     */
    List<BloodDonationDetailDto> getDetailsByMemberId(String memberId);

    /**
     * Delete donation detail by Id
     *
     * @param donationUniqueId
     */
    void delete(String donationUniqueId);

    /**
     * Get top donors by Period (MONTH, YEAR)
     * @param period
     * @param month
     * @param year
     * @param limit
     * @return
     */
    List<DonorStatDto> getTopDonorsByPeriod(StatsPeriod period, int month, int year, int limit);
}
