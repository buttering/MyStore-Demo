package com.csu.mypetstore.api.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;

public record PostCartItemDTO(
        @NotBlank(message = "产品id不能为空")
        Integer productId,
        @Range(min = 1, message = "商品数量不能小于1")
        Integer quantity
){ }
