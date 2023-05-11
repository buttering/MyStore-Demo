package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Order(
        @TableId(type = IdType.AUTO)
        Integer id,
        @TableField(value = "order_no")
        @NotBlank(message = "订单号不能为空")
        Long orderNo,
        @NotBlank(message = "用户id不能为空")
        Integer uid,
        @NotBlank(message = "地址id不能为空")
        Integer addressId,

        @TableField(value = "payment_price")
        @NotBlank(message = "付款价格不能为空")
        BigDecimal paymentPrice,
        Integer paymentType,
        @NotBlank(message = "邮费不能为空")
        Integer postage,
        Integer status,

        @TableField(value = "send_time")
        LocalDateTime sendTime,
        @TableField(value = "end_time")
        LocalDateTime endTime,
        @TableField(value = "close_time")
        LocalDateTime closeTime,

        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updateTime
) { }
