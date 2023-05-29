package com.csu.mypetstore.api.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.csu.mypetstore.api.domain.CartItem;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Integer id;
    private Long orderNo;
    private Integer uid;

    private BigDecimal paymentPrice;
    private Integer paymentType;
    private Integer postage;

    private Integer status;

    @JsonProperty("address")
    private AddressVO addressVO;

    @JsonProperty("orderItemList")
    private List<OrderItemVO> orderItemVOList;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sendTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime closeTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateTime;
}
