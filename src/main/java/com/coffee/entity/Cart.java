package com.coffee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart { // 장바구니 엔터티
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id ;

    // 고객 1명이 1개의 Cart를 사용합니다.
    @OneToOne(fetch = FetchType.LAZY) // delay loading, 연관 관계 매핑
    @JoinColumn(name = "member_id")
    private Member member ;

    // 카트 1개에는 여러 개의 상품들이 담길 수 있으므로 컬렉션 형태로 작성해야 합니다.
    // Cart 엔터티의 변경 사항(저장, 삭제, 수정 등등)을 CartProduct에도 적용하도록 합니다.
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartProduct> cartProducts ;
}
