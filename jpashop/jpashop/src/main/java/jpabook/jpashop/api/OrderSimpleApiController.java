package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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

    // V1 문제 해결 -> entity 외부 노출 해결
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        // N + 1 -> 1(order 조회) + 회원 N(지연 로딩 조회 N번) + 배송 N 문제 발생(지연 로딩 조회 N번)
        // 1개의 쿼리 결과로 N개가 나오는데 그만큼 추가 쿼리 발생한다는 뜻.
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    // V2 문제 해결 -> N + 1 문제 해결 -> fetch 조인으로 쿼리 1번으로 해결함.
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        // FETCH 조인으로 대부분 JPA 성능 문제 해결됨.
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    /*
        쿼리 방식 선택 권장 순서
        1. 우선 엔티티를 DTO로 변환하는 방법 선택 (v2)
        2. 필요하면 fetch 조인으로 성능을 최적화 (v3)
        3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용 (v4)
        4. 최후의 방법으론 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용하여, SQL을 직접 사용
     */
    // new 명령어를 사용하여, JPQL의 결과를 DTO로 즉시 변환하여 원하는 값을 선택하여 조회 가능 (네트웍 용량 최적화 가능하나 생각보다 미비하다.)
    // 리포지토리 재사용성이 떨어지며, API 스펙에 맞춘 코드가 repository에 들어가는 것이 단점
    // 고객이 자주 호출하거나 Select 하는 컬럼이 10개 이상일 경우에는 v4를 고려
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();         // LAZY 초기화 (영속성 컨텍스트에서 찾는데 없으면 쿼리를 날림)
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
