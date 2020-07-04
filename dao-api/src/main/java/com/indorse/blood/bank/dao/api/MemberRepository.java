package com.indorse.blood.bank.dao.api;

import com.indorse.blood.bank.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

    Member findByMemberIdOrEmail(String memberId, String email);

}
