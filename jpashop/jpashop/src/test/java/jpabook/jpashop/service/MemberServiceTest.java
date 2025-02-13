package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional  // 테스트 케이스일 경우 마지막에 롤백함.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;
    
    @Test
    //@Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        // em.flush(); // DB에 강제로 Query가 날려서 Insert Query 확인가능.
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void 중복_회원_예약() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
            return;
        });

        // Junit5로 변경되면서 필요 위처럼 변경됨
        // fail("예외가 발생해야 한다.");
    }
}