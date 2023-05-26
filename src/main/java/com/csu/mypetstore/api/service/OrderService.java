package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.OrderVO;

public interface OrderService {
    public CommonResponse<OrderVO> createOrder (Integer userId, Integer addressId, Integer productId, Integer quantity);

    public CommonResponse<OrderVO> createOrder (Integer userId, Integer addressId);
}
