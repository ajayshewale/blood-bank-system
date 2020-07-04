package com.indorse.blood.bank.service.impl;

import com.indorse.blood.bank.dao.api.MemberRepository;
import com.indorse.blood.bank.model.Member;
import com.indorse.blood.bank.model.constant.BloodGroup;
import com.indorse.blood.bank.model.exception.BloodBankException;
import com.indorse.blood.bank.rest.web.model.MemberDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.indorse.blood.bank.model.constant.ErrorCode.CRUD_EMPTY_ENTITY_ERROR;
import static com.indorse.blood.bank.model.constant.ErrorCode.NEW_ENTITY_WITH_ID_CODE;
import static com.indorse.blood.bank.service.impl.MemberServiceImpl.MEMBER_ID_PREFIX;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class MemberServiceImplTest {

    private static final String MEMBER_ID = "memberId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final BloodGroup BLOOD_GROUP = BloodGroup.AP;
    public static final Long ID = Long.valueOf(1);
    private static final String MEMBER_EMAIL = "email@email.com";

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Captor
    private ArgumentCaptor<Member> memberArgumentCaptor;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    public void test_addMember_nullDto_fail(){
        try {
            memberService.add(null);
        } catch (BloodBankException e){
            assertEquals(e.getErrorCode(), CRUD_EMPTY_ENTITY_ERROR);
        }
    }

    @Test
    public void test_addMember_withId_fail(){
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(MEMBER_ID);
        try {
            memberService.add(memberDto);
        } catch (BloodBankException e){
            assertEquals(e.getErrorCode(), NEW_ENTITY_WITH_ID_CODE);
        }
    }

    @Test
    public void test_addMember_success(){
        MemberDto memberDto = new MemberDto();
        memberDto.setFirstName(FIRST_NAME);
        memberDto.setBloodGroup(BLOOD_GROUP);
        Member member = new Member();
        member.setFirstName(FIRST_NAME);
        member.setBloodGroup(BLOOD_GROUP);
        member.setId(ID);
        when(memberRepository.save(member)).thenReturn(member);

        MemberDto savedMember = memberService.add(memberDto);
        assertEquals(savedMember.getMemberId(), MEMBER_ID_PREFIX+1);
        verify(memberRepository, times(2)).save(any());
    }

    @Test
    public void test_update_success(){
        MemberDto memberDto = new MemberDto();
        memberDto.setFirstName(FIRST_NAME);
        memberDto.setLastName(LAST_NAME);
        memberDto.setBloodGroup(BLOOD_GROUP);
        memberDto.setMemberId(MEMBER_ID_PREFIX+1);
        memberDto.setEmail(MEMBER_EMAIL);
        Member member = new Member();
        member.setFirstName(FIRST_NAME);
        member.setBloodGroup(BLOOD_GROUP);
        member.setId(ID);
        member.setMemberId(MEMBER_ID_PREFIX+1);
        when(memberRepository.findByMemberIdOrEmail(anyString(), anyString())).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(member);
        memberService.update(memberDto);
        verify(memberRepository).save(any());
        verify(memberRepository).findByMemberIdOrEmail(memberDto.getMemberId(), memberDto.getEmail());
    }

    @Test
    public void test_delete_success(){
        Member member = new Member();
        member.setFirstName(FIRST_NAME);
        member.setBloodGroup(BLOOD_GROUP);
        member.setId(ID);
        member.setMemberId(MEMBER_ID_PREFIX+1);
        when(memberRepository.findByMemberIdOrEmail(anyString(), anyString())).thenReturn(member);
        memberService.deleteByMemberIdOrEmail(MEMBER_ID, MEMBER_EMAIL);
        verify(memberRepository).save(memberArgumentCaptor.capture());
        assertTrue(memberArgumentCaptor.getValue().getMarkForDelete());
        verify(memberRepository).findByMemberIdOrEmail(MEMBER_ID, MEMBER_EMAIL);
    }

    @Test
    public void test_getByMemberIdOrEmail_success(){
        Member member = new Member();
        member.setFirstName(FIRST_NAME);
        member.setBloodGroup(BLOOD_GROUP);
        member.setId(ID);
        member.setMemberId(MEMBER_ID_PREFIX+1);
        when(memberRepository.findByMemberIdOrEmail(anyString(), anyString())).thenReturn(member);
        MemberDto memberDto = memberService.getByMemberIdOrEmail(MEMBER_ID, MEMBER_EMAIL);
        assertEquals(memberDto.getBloodGroup(), BLOOD_GROUP);
        verify(memberRepository).findByMemberIdOrEmail(MEMBER_ID, MEMBER_EMAIL);
    }
}