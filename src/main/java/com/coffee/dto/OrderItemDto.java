package com.coffee.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 주문이 일어날 때 1건의 상품 정보
@Getter @Setter
@ToString
public class OrderItemDto {
    // cartProductId 변수는 `카트 목록 보기(CartList)` 메뉴에서만 사용이 되고 있습니다.
    private Long cartProductId ; // 카트 상품 번호
    private Long productId ; // 상품 번호
    private int quantity; // 구매 수량
}
