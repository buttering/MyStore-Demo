package com.csu.mypetstore.api.domain.vo;

import java.math.BigDecimal;
import java.util.List;

public record CartVO(
        List<CartItemVO> cartItemVOList,
        BigDecimal cartTotalPrice,
        Boolean allSelected
) {
}
