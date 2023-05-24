package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.dto.PostCartItemDTO;
import com.csu.mypetstore.api.domain.vo.CartVO;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;
import com.csu.mypetstore.api.service.CartService;
import com.csu.mypetstore.api.util.ValidGroup;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CartController {
    private final CartService cartItemService;

    public CartController(CartService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("api/cartitem")
    public CommonResponse<CartVO> addCartItem(@Validated(ValidGroup.AddCartItem.class) @RequestBody PostCartItemDTO cartItem, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        return cartItemService.addCartItem(loginUser.id(), cartItem.productId(), cartItem.quantity());
    }

    @DeleteMapping("api/cartitem")
    public CommonResponse<CartVO> deleteCartItem(@Validated @RequestBody PostCartItemDTO cartItem, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        return cartItemService.deleteCartItem(loginUser.id(), cartItem.productId());
    }

    @GetMapping("api/cart")
    public CommonResponse<CartVO> getCart(HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        return cartItemService.getCart(loginUser.id());
    }

    @PatchMapping("api/cartitem")
    public CommonResponse<CartVO> updateCartItem(@Validated @RequestBody PostCartItemDTO cartItem, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());
        if (cartItem.selected() == null && cartItem.quantity() == null)
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.ARGUMENT_ILLEGAL.getCode());

        return cartItemService.updateCartItem(loginUser.id(), cartItem.productId(), cartItem.quantity(), cartItem.selected());
    }

    @PatchMapping("api/cart")
    public CommonResponse<CartVO> selectAllCart(@RequestBody Map<String,Boolean> params, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        Boolean allSelected = params.get("allSelected");
        if (allSelected == null)
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.ARGUMENT_ILLEGAL.getCode());

        return cartItemService.updateCartSelect(loginUser.id(), allSelected);
    }

    @GetMapping("api/cart/number")
    public CommonResponse<Integer> getCartItemNumber(HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        return cartItemService.getCartItemNum(loginUser.id());
    }
}
