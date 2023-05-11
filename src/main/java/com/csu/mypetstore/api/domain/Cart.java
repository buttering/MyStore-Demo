package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record Cart (
        @TableId(type = IdType.AUTO)
        Integer id,
        @NotBlank(message = "用户id不能为空")
        Integer uid,
        @TableField(value = "product_id")
        @NotBlank(message = "产品id不能为空")
        Integer productId,

        @NotBlank(message = "数量不能为空")
        Integer quantity,

        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updateTime
) { }
