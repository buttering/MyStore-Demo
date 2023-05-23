package com.csu.mypetstore.api.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;


public record CartVO(
        @JsonProperty("cartItemList")
        List<CartItemVO> cartItemVOList,
        BigDecimal cartTotalPrice,
        Boolean allSelected
) {
}
