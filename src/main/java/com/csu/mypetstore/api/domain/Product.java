package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
        @TableId(type = IdType.AUTO)
        Integer id,
        @TableField(value = "category_id")
        @NotNull(message = "产品类别id不能为空")
        Integer categoryId,

        @NotBlank(message = "产品名不能为空")
        String name,
        String subtitle,
        @TableField(value = "main_image")
        String mainImage,
        @TableField(value = "sub_image")
        String subImage,
        String detail,
        @NotNull(message = "价格不能为空")
        BigDecimal price,
        @NotNull(message = "库存不能为空")
        Integer stock,
        Integer status,

        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updateTime

) { }