package com.csu.mypetstore.api.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemVO {
    private Integer id;

    private Integer productId;
    private String productName;
    private String productSubtitle;
    private BigDecimal currentPrice;
    private Integer quantity;
    private Integer totalPrice;
}
