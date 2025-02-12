package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable // 내장 타입을 지시하는 어노테이션 (Address 또는 Member에서 호출하는 변수 중 하나만 지시해도 되나 둘다 지시하는게 좋음)
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    // Spec상 Protected로 선언,
    protected Address() {
    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
