package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Product(
        @TableId(type = IdType.AUTO)
        Integer id,
        @TableField(value = "category_id")
        @NotBlank(message = "产品类别id不能为空")
        Integer categoryId,

        @NotBlank(message = "产品名不能为空")
        String name,
        String subtitle,
        @TableField(value = "main_image")
        String mainImage,
        @TableField(value = "sub_image")
        String subImage,
        String detail,
        @NotBlank(message = "价格不能为空")
        BigDecimal price,
        @NotBlank(message = "库存不能为空")
        Integer stock,
        Integer status,

        @TableField(value = "create_time")
        @NotBlank(message = "创建时间不能为空")
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @NotBlank(message = "更新时间不能为空")
        LocalDateTime updateTime

) { }