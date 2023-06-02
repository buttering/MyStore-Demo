package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@TableName(value = "payinfo")
public class PayInfo {
        @TableId(type = IdType.AUTO)
        private Integer id;
        private @NotBlank(message = "用户id不能为空") Integer uid;
        private @NotBlank(message = "订单编号不能为空") Long orderNo;

        @TableField(value = "payment_type")
        private Integer paymentType;
        @TableField(value = "trade_no")
        private String tradeNo;
        @TableField(value = "trade_status")
        private String tradeStatus;
        @TableField(value = "create_time")

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime createTime;
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime updateTime;

        @Override
        public String toString() {
                return "PayInfo[" +
                        "id=" + id + ", " +
                        "uid=" + uid + ", " +
                        "orderNo=" + orderNo + ", " +
                        "paymentType=" + paymentType + ", " +
                        "tradeNo=" + tradeNo + ", " +
                        "tradeStatus=" + tradeStatus + ", " +
                        "createTime=" + createTime + ", " +
                        "updateTime=" + updateTime + ']';
        }
}
