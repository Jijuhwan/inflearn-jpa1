package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // PersistenceContext 원래 이 어노테이션을 사용해야 하지만 SpringDataJPA에서 지원해주어서 @Autowird로 해도 됨
    private final EntityManager em;

    /*
    // RequiredArgsConstructor 방식으로 일관성 있게 사용하기 위해 주석
    @PersistenceContext
    private EntityManager em;
     */

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
