package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@TableName("`order`")
public class Order {
        @TableId(type = IdType.AUTO)
        private Integer id;
        @TableField(value = "order_no")
        private @NotBlank(message = "订单号不能为空") Long orderNo;
        private @NotBlank(message = "用户id不能为空") Integer uid;
        private @NotBlank(message = "地址id不能为空") Integer addressId;

        @TableField(value = "payment_price")
        private @NotBlank(message = "付款价格不能为空") BigDecimal paymentPrice;
        private Integer paymentType;
        private @NotBlank(message = "邮费不能为空") Integer postage;
        private Integer status;

        @TableField(value = "payment_time")
        private LocalDateTime paymentTime;
        @TableField(value = "send_time")
        private LocalDateTime sendTime;
        @TableField(value = "end_time")
        private LocalDateTime endTime;
        @TableField(value = "close_time")
        private LocalDateTime closeTime;
        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime createTime;
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime updateTime;

        @Override
        public String toString() {
                return "Order[" +
                        "id=" + id + ", " +
                        "orderNo=" + orderNo + ", " +
                        "uid=" + uid + ", " +
                        "addressId=" + addressId + ", " +
                        "paymentPrice=" + paymentPrice + ", " +
                        "paymentType=" + paymentType + ", " +
                        "postage=" + postage + ", " +
                        "status=" + status + ", " +
                        "paymentTime=" + paymentTime + ", " +
                        "sendTime=" + sendTime + ", " +
                        "endTime=" + endTime + ", " +
                        "closeTime=" + closeTime + ", " +
                        "createTime=" + createTime + ", " +
                        "updateTime=" + updateTime + ']';
        }
}
