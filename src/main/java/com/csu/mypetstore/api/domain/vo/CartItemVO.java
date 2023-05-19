package com.csu.mypetstore.api.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class CartItemVO {
    private Integer id;
    private Integer uid;
    private Integer productId;
    private Integer quantity;
    private Boolean checked;

    private String productName;
    private String productSubtitle;
    private String productPrice;
    private String productStock;

    private List<Map<String, Object>> imageList;

    private String productTotalPrice;
}
