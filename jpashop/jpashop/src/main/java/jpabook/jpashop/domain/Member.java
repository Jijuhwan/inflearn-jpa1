package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded   // 내장 타입을 지시하는 어노테이션 (Address 또는 Member에서 호출하는 변수 중 하나만 지시해도 되나 둘다 지시하는게 좋음)
    private Address address;

    @OneToMany(mappedBy = "member") // 읽기 전용이 되며, Order가 주인이됨.
    private List<Order> orders = new ArrayList<>(); // 필드에서 초기화시 가장 좋은 형태

    /* 아래 같은 방식으로 초기화할 수 있지만 현재 형태가 좋음.
    public Member() {
        orders = new ArrayList<>();
    }
     */
}
