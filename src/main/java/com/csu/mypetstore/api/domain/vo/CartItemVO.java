package com.csu.mypetstore.api.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class CartItemVO {
    private Integer id;
    private Integer uid;
    private Integer productId;
    private Integer quantity;
    private Boolean selected;

    private String productName;
    private String productSubtitle;
    private BigDecimal productPrice;
    private Integer productStock;

    private List<Map<String, Object>> imageList;
    private BigDecimal productTotalPrice;
    private int checkStock;  // 购物车内数量是否超过了商品库存
}