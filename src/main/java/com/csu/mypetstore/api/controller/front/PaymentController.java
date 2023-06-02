package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.PayInfo;
import com.csu.mypetstore.api.domain.vo.QRCodeVO;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;
import com.csu.mypetstore.api.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("api/pay")
    public CommonResponse<QRCodeVO> preCreateTrade(@RequestBody Map<String, Long> params, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        Long orderNo = params.get("order_no");
        if (orderNo == null)
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.ARGUMENT_ILLEGAL.getCode());

        return paymentService.tradePrecreate(loginUser.id(), orderNo);
    }

    @GetMapping("api/pay")
    public CommonResponse<PayInfo> checkPayment(@RequestParam(value = "order_no") Long orderNo, HttpSession session) {
        UserInfoVO loginUser = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (loginUser == null)
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        return paymentService.checkPaymentAndUpdateOrder(loginUser.id(), orderNo);
    }
}
