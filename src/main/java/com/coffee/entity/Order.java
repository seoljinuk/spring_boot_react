package com.coffee.entity;

import com.coffee.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id ;

    // 1사람이 여러 개의 주문을 할 수 있습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member ;

    // 1개의 주문에는 `주문 상품`이 여러 개 가능합니다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts ;

    @Column(nullable = false)
    private LocalDate orderDate ; // 주문 날짜

    @Enumerated(EnumType.STRING)
    private OrderStatus status ; // 주문 상태
}
