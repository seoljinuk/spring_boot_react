package com.coffee.controller;

import com.coffee.dto.OrderItemDto;
import com.coffee.dto.OrderRequestDto;
import com.coffee.dto.OrderResponseDto;
import com.coffee.entity.Member;
import com.coffee.entity.Order;
import com.coffee.entity.OrderProduct;
import com.coffee.entity.Product;
import com.coffee.repository.MemberRepository;
import com.coffee.repository.OrderRepository;
import com.coffee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// @Controller // 스프링 부트에서 리액트 대신 html 문서로 만들 때 사용 바람
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto dto){
        // 회원(Member) 객체 생성
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));

        // 주문(Order) 객체 생성
        Order order = new Order();
        order.setMember(member);
        order.setOrderDate(LocalDate.now());
        order.setStatus(dto.getStatus());

        // 확장 for 사용 주문 상품(OrderProduct) 객체 생성
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (OrderItemDto item : dto.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("재고가 부족합니다.");
            }

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(item.getQuantity());

            product.setStock(product.getStock() - item.getQuantity());
            orderProducts.add(orderProduct);
        }
        order.setOrderProducts(orderProducts);

        // 주문 객체 저장
        orderRepository.save(order);
        return ResponseEntity.ok("주문이 완료되었습니다.");
    }

    /**
     * 로그인한 회원의 주문 목록 조회
     * /order/list?memberId=1
     * "/order/list/{id}")
     */
    @GetMapping("/list")
    public ResponseEntity<List<OrderResponseDto>> getOrderList(@RequestParam Long memberId) {
        List<Order> orders = orderRepository.findByMemberId(memberId);

        // Entity -> DTO 변환
        List<OrderResponseDto> response = orders.stream().map(order -> {
            OrderResponseDto dto = new OrderResponseDto();
            dto.setOrderId(order.getId());
            dto.setOrderDate(order.getOrderDate());
            dto.setStatus(order.getStatus().name());
            dto.setOrderItems(order.getOrderProducts().stream()
                    .map(op -> new OrderResponseDto.OrderItem(
                            op.getProduct().getName(),
                            op.getQuantity()
                    )).collect(Collectors.toList())
            );
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
