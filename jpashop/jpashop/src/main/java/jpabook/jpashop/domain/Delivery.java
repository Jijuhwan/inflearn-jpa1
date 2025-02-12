package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY) // mappedBy 요렇게 작성하면 연관관계를 매핑함.
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)   // Default로 ORDINAL이 지정되며 ORDINAL(숫자)은 숫자타입이다.
    private DeliveryStatus status; //READY, COMP
}
