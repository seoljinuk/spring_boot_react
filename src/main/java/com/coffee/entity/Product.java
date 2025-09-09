package com.coffee.entity;

import com.coffee.constant.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// 상품 1개를 의미하는 자바 클래스
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    // 엔터티 코딩 작성시 실제 제약 조건도 고려해야 합니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id ;

    @Column(nullable = false)
    private String name ;

    @Column(nullable = false)
    private int price = 0 ;

    @Enumerated(EnumType.STRING)
    private Category category ;

    @Column(nullable = false)
    private int stock = 0 ;

    @Column(nullable = false)
    private String image ;

    @Column(nullable = false, length = 1000)
    private String description ;

    private LocalDate inputdate ;
}
