package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@TableName(value = "payinfo")
public record PayInfo(
        @TableId(type = IdType.AUTO)
        Integer id,
        @NotBlank(message = "用户id不能为空")
        Integer uid,
        @NotBlank(message = "订单编号不能为空")
        Long orderNo,

        @TableField(value = "payment_type")
        Integer paymentType,
        @TableField(value = "trade_no")
        String tradeNo,
        @TableField(value = "trade_status")
        String tradeStatus,

        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updateTime
) { }
