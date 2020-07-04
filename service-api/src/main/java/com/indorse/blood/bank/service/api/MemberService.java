package com.indorse.blood.bank.service.api;

import com.indorse.blood.bank.model.Member;
import com.indorse.blood.bank.rest.web.model.MemberDto;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {

    /**
     * Add member to the catalog
     * @param memberDto
     * @return memberCode
     */
    MemberDto add(MemberDto memberDto);

    /**
     * Update existing memberDetail
     * @param memberDto
     */
    void update(MemberDto memberDto);

    /**
     * Delete member by memberCode or email
     * @param memberId
     * @param email
     */
    void deleteByMemberIdOrEmail(String memberId, String email);

    /**
     * Get memberDetail by memberCode or email
     * @param memberId
     * @param email
     * @return
     */
    MemberDto getByMemberIdOrEmail(String memberId, String email);

    /**
     * Get Member by memberId
     * @param memberId
     * @return
     */
    Member getByMemberId(String memberId);

}
