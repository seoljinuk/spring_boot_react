package com.coffee.dto;

import com.coffee.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

// 주문 1건의 최소 단위
@Getter
@Setter
@ToString
public class OrderRequestDto {
    private Long memberId ;
    private OrderStatus status ;
    private List<OrderItemDto> orderItems ;
}
