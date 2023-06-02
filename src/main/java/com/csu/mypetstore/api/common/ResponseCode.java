package com.csu.mypetstore.api.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    ARGUMENT_ILLEGAL(100, "ARGUMENT ILLEGAL"),
    NEED_LOGIN(11, "NEED_LOGIN"),
    EMPTY_OBJECT(12, "EMPTY OBJECT"),
    PAID_ORDER(13, "订单已支付"),  // 已支付的订单，需要退款才能取消
    ORDER_NOT_EXIST(14, "订单不存在");  // 订单不存在，和支付相关的地方有时需要特别处理


    private final int code;
    private final String description;

    ResponseCode(int code, String description){
        this.code = code;
        this.description = description;
    }
}
