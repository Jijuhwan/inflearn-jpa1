package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 아래 정도만 연관이 걸리도록 구현
 * xToOne(ManyToOne, OneToOne) 에서의 성능 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    // 엔티티를 직접 노출하는 것은 안좋다. entity 변경시 API Spec이 변경되어야 함 -> 결국 DTO로 변환해서 반환이 좋음.
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();         // Lazy 강제 초기화
            order.getDelivery().getAddress();    // Lazy 강제 초기화
        }
        return all;
    }
}
