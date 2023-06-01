package com.csu.mypetstore.api.domain.vo;

import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@With
public record OrderListVO(
        Integer id,
        Long orderNo,

        BigDecimal paymentPrice,
        Integer status,
        List<String> productNameList,

        @JsonProperty("address")
        AddressVO addressVO,

        @JsonSerialize(using =LocalDateTimeSerializer.class)
        LocalDateTime updateTime
) {}
