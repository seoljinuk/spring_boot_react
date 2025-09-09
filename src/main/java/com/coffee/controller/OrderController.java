package com.coffee.controller;

import com.coffee.dto.OrderItemDto;
import com.coffee.dto.OrderRequestDto;
import com.coffee.entity.Member;
import com.coffee.entity.Order;
import com.coffee.entity.OrderProduct;
import com.coffee.entity.Product;
import com.coffee.repository.MemberRepository;
import com.coffee.repository.OrderRepository;
import com.coffee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
}
