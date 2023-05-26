package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@TableName(value = "orderitem")
public class OrderItem {
        @TableId(type = IdType.AUTO)
        private Integer id;
        private @NotBlank(message = "用户id不能为空") Integer uid;
        @TableField(value = "order_no")
        private @NotBlank(message = "订单编号不能为空") Long orderNo;
        @TableField(value = "product_id")
        private @NotBlank(message = "产品id不能为空") Integer productId;
        @TableField(value = "product_name")

        private @NotBlank(message = "商品名不能为空") String productName;
        @TableField(value = "product_subtitle")
        private String productSubtitle;
        @TableField(value = "current_price")
        private @NotBlank(message = "购买单价不能为空") BigDecimal currentPrice;
        private @NotBlank(message = "购买数量不能为空") Integer quantity;
        @TableField(value = "total_price")
        private @NotBlank(message = "总价不能为空") BigDecimal totalPrice;

        @TableField(value = "create_time", fill = FieldFill.INSERT)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime createTime;
        @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime updateTime;

        @Override
        public String toString() {
                return "OrderItem[" +
                        "id=" + id + ", " +
                        "uid=" + uid + ", " +
                        "orderNo=" + orderNo + ", " +
                        "productId=" + productId + ", " +
                        "productName=" + productName + ", " +
                        "productSubtitle=" + productSubtitle + ", " +
                        "currentPrice=" + currentPrice + ", " +
                        "quantity=" + quantity + ", " +
                        "totalPrice=" + totalPrice + ", " +
                        "createTime=" + createTime + ", " +
                        "updateTime=" + updateTime + ']';
        }
}