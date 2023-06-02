package com.csu.mypetstore.api.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.Order;
import com.csu.mypetstore.api.domain.PayInfo;
import com.csu.mypetstore.api.domain.vo.QRCodeVO;
import com.csu.mypetstore.api.persistence.OrderMapper;
import com.csu.mypetstore.api.persistence.PayInfoMapper;
import com.csu.mypetstore.api.service.PaymentService;
import com.csu.mypetstore.api.util.BigDecimalUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
public class AliPayServiceImpl implements PaymentService {
    private final OrderMapper orderMapper;
    private final PayInfoMapper payInfoMapper;

    public AliPayServiceImpl(OrderMapper orderMapper, PayInfoMapper payInfoMapper) {
        this.orderMapper = orderMapper;
        this.payInfoMapper = payInfoMapper;
    }

    @Override
    public CommonResponse<QRCodeVO> tradePrecreate(Integer userId, Long orderNo) {
        Order order = orderMapper.selectOne(Wrappers.<Order>query().eq("uid", userId).eq("order_no", orderNo));
        if (order == null) return CommonResponse.createResponseForError(ResponseCode.ORDER_NOT_EXIST.getDescription(), ResponseCode.ORDER_NOT_EXIST.getCode());
        if (order.getStatus() == CONSTANT.OrderStatus.CANCEL.getCode()) return CommonResponse.createResponseForError("订单已取消");
        if (order.getStatus() > CONSTANT.OrderStatus.UNPAID.getCode()) return CommonResponse.createResponseForError("订单已支付");

        // 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
        String out_trade_no = orderNo.toString();
        // 订单总金额，单位为元，精确到小数点后两位，取值范围为 [0.01,100000000]，金额不能为 0。
        String total_amount = BigDecimalUtils.add(order.getPaymentPrice().doubleValue(), order.getPostage()).toString();
        // 订单标题。注意：不可使用特殊字符，如 /，=，& 等。
        String subject = tradeSubject();

        String qrCode = preCreateFace2Face(out_trade_no, total_amount, subject);
        if (qrCode == null){
            log.error("预创建订单超过重复次数  {}", LocalDateTime.now());
            return CommonResponse.createResponseForError("预创建订单失败");
        }

        return CommonResponse.createResponseForSuccess(new QRCodeVO(orderNo, qrCode));
    }

    private String preCreateFace2Face(String out_trade_no, String total_amount, String subject){
        int maxRetry = 5;

        AlipayTradePrecreateResponse response = new AlipayTradePrecreateResponse();
        for (int i = 1; i <= maxRetry; i ++){
            try {
                response = Factory.Payment.FaceToFace().preCreate(subject, out_trade_no, total_amount);
                if (ResponseChecker.success(response))
                    break;
            } catch (Exception e) {
                log.error("预创建订单失败, 重试次数:{}", i);
                log.info(e.toString());
            }
        }
        return response.getQrCode();
    }

    // 订单标题
    private String tradeSubject() {
        return "商城订单";
    }

    @Override
    public CommonResponse<PayInfo> checkPaymentAndUpdateOrder(Integer userId, Long orderNo) {
        Order order = orderMapper.selectOne(Wrappers.<Order>query().eq("uid", userId).eq("order_no", orderNo));
        if (order == null) return CommonResponse.createResponseForError(ResponseCode.ORDER_NOT_EXIST.getDescription(), ResponseCode.ORDER_NOT_EXIST.getCode());

        AlipayTradeQueryResponse response;
        try {
            response = Factory.Payment.Common().query(orderNo.toString());
            if (!ResponseChecker.success(response)) throw new RuntimeException(response.code + response.subMsg);
        } catch (Exception e) {
            log.error("查询订单失败，订单号{}   {}", orderNo, LocalDateTime.now());
            log.info(e.getMessage());
            return CommonResponse.createResponseForError("查询订单失败");
        }

        if (!Objects.equals(response.tradeStatus, "TRADE_SUCCESS"))
            return CommonResponse.createResponseForError("订单未支付");

        LocalDateTime now = LocalDateTime.now();
        order.setUpdateTime(now);
        order.setPaymentTime(now);
        order.setPaymentType(CONSTANT.PaymentType.ALIPAY_FACE2FACE);
        order.setStatus(CONSTANT.OrderStatus.PAID.getCode());
        orderMapper.updateById(order);

        PayInfo payInfo = new PayInfo(null, userId, orderNo, CONSTANT.PaymentType.ALIPAY_FACE2FACE, response.tradeNo, response.tradeStatus, now, now);
        payInfoMapper.insert(payInfo);
        return CommonResponse.createResponseForSuccess(payInfo);
    }
}
