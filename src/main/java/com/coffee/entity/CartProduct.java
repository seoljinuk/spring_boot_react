package com.coffee.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_products")
public class CartProduct { // 장바구니에 담을 상품 정보 엔터티
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_product_id")
    private Long id ;

    // 카트 1개에는 여러 개의 품목들이 들어 갈수 있습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id", nullable = false)
    private Cart cart ;

    // 상품 유형 1개는 여러 카트에 담길 수 있습니다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", nullable = false)
    private Product product ;

    @Column(nullable = false)
    private int quantity ; // 구매 수량
}
