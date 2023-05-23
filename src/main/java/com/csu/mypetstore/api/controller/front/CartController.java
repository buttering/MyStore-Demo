package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.dto.PostCartItemDTO;
import com.csu.mypetstore.api.domain.vo.CartItemVO;
import com.csu.mypetstore.api.domain.vo.CartVO;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;
import com.csu.mypetstore.api.service.CartService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
    private final CartService cartItemService;

    public CartController(CartService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("api/cartitem")
    public CommonResponse<CartVO> addCartItem(@Valid @RequestBody PostCartItemDTO cartItem, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        return cartItemService.addCartItem(loginUser.id(), cartItem.productId(), cartItem.quantity());
    }
}
