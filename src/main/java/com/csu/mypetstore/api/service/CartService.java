package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.vo.CartVO;

public interface CartService {
    CommonResponse<CartVO> addCartItem(Integer userId, Integer productId, Integer quantity);

    CommonResponse<CartVO> updateCartItem(Integer userId, Integer productId, Integer quantity, Boolean selected);

    CommonResponse<CartVO> getCart(Integer userId);

    CommonResponse<CartVO> deleteCartItem(Integer userId, Integer productId);

    CommonResponse<Integer> getCartItemNum(Integer userId);

    CommonResponse<CartVO> updateCartSelect(Integer userId, Boolean allSelected);
}
