package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.dto.PostOrderDTO;
import com.csu.mypetstore.api.domain.vo.OrderListVO;
import com.csu.mypetstore.api.domain.vo.OrderVO;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;
import com.csu.mypetstore.api.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("api/order")
    public CommonResponse<OrderVO> createOrder(@Validated @RequestBody PostOrderDTO postOrder, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        if (postOrder.productId() == null)
            return orderService.createOrder(loginUser.id(), postOrder.addressId());
        if (postOrder.quantity() == null || postOrder.quantity() <= 0)
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.ARGUMENT_ILLEGAL.getCode());
        return orderService.createOrder(loginUser.id(), postOrder.addressId(), postOrder.productId(), postOrder.quantity());
    }

    @DeleteMapping("api/order")
    public CommonResponse<OrderVO> deleteOrder(@RequestBody Map<String, Long> params, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        Long orderNo = params.get("order_no");
        if (orderNo == null)
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.ARGUMENT_ILLEGAL.getCode());

        return orderService.deleteOrder(loginUser.id(), orderNo);
    }

    @GetMapping("api/order")
    public CommonResponse<OrderVO> getOrderById(@RequestParam("order_no") Long orderNo, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        return orderService.getOrderById(loginUser.id(), orderNo);
    }

    @GetMapping("api/orders")
    public CommonResponse<List<OrderListVO>> getOrderList(HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        return orderService.getOrderList(loginUser.id());
    }
}
