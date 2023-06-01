package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.OrderListVO;
import com.csu.mypetstore.api.domain.vo.OrderVO;

import java.util.List;

public interface OrderService {
    CommonResponse<OrderVO> createOrder (Integer userId, Integer addressId, Integer productId, Integer quantity);

    CommonResponse<OrderVO> createOrder (Integer userId, Integer addressId);

    CommonResponse<OrderVO> deleteOrder (Integer userId, Long orderNo);

    CommonResponse<OrderVO> getOrderById (Integer userId, Long orderNo);

    CommonResponse<List<OrderListVO>> getOrderList(Integer userId);
}
