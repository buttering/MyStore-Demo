package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@TableName(value = "cartitem")
public record CartItem(
        @TableId(type = IdType.AUTO)
        Integer id,
        @NotBlank(message = "用户id不能为空")
        Integer uid,
        @TableField(value = "product_id")
        @NotBlank(message = "产品id不能为空")
        Integer productId,

        @NotBlank(message = "数量不能为空")
        Integer quantity,
        @NotBlank(message = "点击状态不能为空")
        Boolean checked,  // TODO 使用缓存，减少数据库写入

        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updateTime
) { }
