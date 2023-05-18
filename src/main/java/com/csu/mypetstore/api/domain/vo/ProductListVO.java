package com.csu.mypetstore.api.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductListVO {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private BigDecimal price;
    private Integer status;

    private List<Map<String, Object>> imageList;
}
