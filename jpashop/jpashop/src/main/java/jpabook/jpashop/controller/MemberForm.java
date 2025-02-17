package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
