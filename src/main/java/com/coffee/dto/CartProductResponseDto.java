package com.coffee.dto;

import com.coffee.entity.CartProduct;
import lombok.Getter;
import lombok.Setter;

// 리액트의 CartList.js 파일 내 fetchCartProducts 함수 참조
// 카트 상품 목록 페이지에서 보여 주는 데이터 1개를 표현하기 위한 DTO 클래스
@Getter @Setter
public class CartProductResponseDto {
    private Long cartProductId ; // 카트 상품 Id로써, `수량 변경`이나 `삭제`시 반드시 사용됩니다.
    private Long productId ; // 상품의 Id
    private String name ; // 상품 이름
    private String image ; // 상품 이미지
    private int price ; // 단가
    private int quantity ; // 구매하고자 하는 수량
    private boolean checked = false ; // 카트 상품 품목에서 체크 여부

    public CartProductResponseDto(CartProduct cartProduct) {
        this.cartProductId = cartProduct.getId() ;
        this.productId = cartProduct.getProduct().getId() ;
        this.name = cartProduct.getProduct().getName() ;
        this.image = cartProduct.getProduct().getImage();
        this.price = cartProduct.getProduct().getPrice();
        this.quantity = cartProduct.getQuantity() ;
    }
}
