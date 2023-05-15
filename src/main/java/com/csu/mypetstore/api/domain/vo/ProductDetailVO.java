package com.csu.mypetstore.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetailVO (
    Integer id,
    Integer categoryId,
    String name,
    String subtitle,
    String mainImage,
    String subImages,
    String detail,
    BigDecimal price,
    Integer stock,
    Integer status,
    
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime createTime,
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime updateTime,

    //该商品的父分类ID
    Integer parentCategoryId,
    //图片服务器的根域
    String imageServer
) {}
