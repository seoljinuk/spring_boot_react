package com.coffee.dto;

import lombok.Getter;
import lombok.Setter;

// Dto : Data Transfer Object (데이터 전송 객체)
// 장바구니에 담기 위하여 리액트가 넘겨 주는 파라미터의 값을 저장하기 위한 클래스
@Getter @Setter
public class CartProductRequestDto {
    private Long memberId; // 회원 아이디
    private Long productId; // 상품 아이디
    private int quantity; // 구매 수량
}
