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
import java.util.List;

public record Product(
        @TableId(type = IdType.AUTO)
        Integer id,
        @TableField(value = "category_id")
        @NotNull(message = "产品类别id不能为空")
        Integer categoryId,

        @NotBlank(message = "产品名不能为空")
        String name,
        String subtitle,
//        @TableField(value = "main_image")
//        String mainImage,
//        @TableField(value = "sub_image")
//        List<String> subImageList,"
        String detail,
        @NotNull(message = "价格不能为空")
        BigDecimal price,  // 涉及价格、总价等浮点型数据的运算必须使用BigDecimal类处理
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