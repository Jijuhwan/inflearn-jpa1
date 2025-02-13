package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor    // 필드가 있는 생성자들만 생성해준다.
public class MemberService {

    private final MemberRepository memberRepository; // final 권장 -> 컴파일 시점에 값 세팅 여부가 검증되기에 추천

    // 생성자 인젝션 -> RequiredArgsConstructor 가 있기에 불필요
    /*
    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    */

    /*
    @Autowired
    private MemberRepository memberRepository; // 필드 인젝션

    // Setter 인젝션 -> RunTime시 누군가 수정할 수 있음.
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
     */

    /**
     * 회원 가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);    // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 아래와 같이 검증을 하여도, 멀티스레딩 환경에선 동시에 접근이 가능할 수 있기에 DB에서 유니크 키로 잡는게 좋다.
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    // @Transactional(readOnly = true) // 읽기 전용일때 이점이 있음,
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }

}
