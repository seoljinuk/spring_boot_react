package com.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private LocalDate orderDate;
    private String status;
    private List<OrderItem> orderItems;

    @Data
    @AllArgsConstructor
    public static class OrderItem {
        private String productName;
        private int quantity;
    }
}
