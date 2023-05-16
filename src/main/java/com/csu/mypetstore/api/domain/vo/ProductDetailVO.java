package com.csu.mypetstore.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.csu.mypetstore.api.domain.ProductImage;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public record ProductDetailVO (
    Integer id,
    Integer categoryId,
    String name,
    String subtitle,
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

    // 图片信息及对应临时密钥
    List<Map<String, Object>> imageList
) {
    public ProductDetailVO addImage(ProductImage image, TencentCOSVO token) {
        List<Map<String, Object>> imageList;
        imageList = Objects.requireNonNullElseGet(this.imageList, ArrayList::new);
        Map<String, Object> imageMap = new HashMap<>();
        imageMap.put("token", token);
        imageMap.put("image", image);
        imageList.add(imageMap);
        return new ProductDetailVO(id, categoryId, name, subtitle, detail, price, stock, status, createTime, updateTime, parentCategoryId, imageList);
    }
}

