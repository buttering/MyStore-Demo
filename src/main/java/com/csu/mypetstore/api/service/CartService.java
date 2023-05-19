package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.CartVO;

public interface CartService {
    public CommonResponse<CartVO> addCartItem(Integer userId, Integer productId, Integer quantity);

    public CommonResponse<CartVO> updateCart(Integer userId, Integer productId, Integer quantity);

    public CommonResponse<CartVO> getCart(Integer userId);

    public CommonResponse<CartVO> deleteCart(Integer userId, Integer productId, Integer quantity);
}
