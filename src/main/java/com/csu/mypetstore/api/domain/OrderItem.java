package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName(value = "orderitem")
public record OrderItem(
        @TableId(type = IdType.AUTO)
        Integer id,
        @NotBlank(message = "用户id不能为空")
        Integer uid,
        @TableField(value = "order_no")
        @NotBlank(message = "订单编号不能为空")
        Long orderNo,
        @TableField(value = "product_id")
        @NotBlank(message = "产品id不能为空")
        Integer ProductId,

        @TableField(value = "product_name")
        @NotBlank(message = "商品名不能为空")
        String productName,
        @TableField(value = "product_image")
        String ProductImage,
        @TableField(value = "current_price")
        @NotBlank(message = "购买单价不能为空")
        BigDecimal currentPrice,
        @NotBlank(message = "购买数量不能为空")
        Integer quantity,
        @TableField(value = "total_price")
        @NotBlank(message ="总价不能为空" )
        BigDecimal totalPrice,

        @TableField(value = "create_time")
        @NotBlank(message = "创建时间不能为空")
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @NotBlank(message = "更新时间不能为空")
        LocalDateTime updateTime
) { }