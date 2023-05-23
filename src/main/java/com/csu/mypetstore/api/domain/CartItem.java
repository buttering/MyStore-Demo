package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

// TODO 使用缓存，减少数据库写入
@TableName(value = "cartitem")
@Data
@AllArgsConstructor
public class CartItem {
        @TableId(type = IdType.AUTO)
        private Integer id;
        @NotBlank(message = "用户id不能为空")
        private Integer uid;
        @TableField(value = "product_id")
        @NotBlank(message = "产品id不能为空")
        private Integer productId;

        @NotBlank(message = "数量不能为空")
        private Integer quantity;
        @NotBlank(message = "状态不能为空")
        private int status;
        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime createTime;
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private LocalDateTime updateTime;
}
