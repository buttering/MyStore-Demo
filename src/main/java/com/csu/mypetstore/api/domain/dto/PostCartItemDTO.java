package com.csu.mypetstore.api.domain.dto;

import com.csu.mypetstore.api.util.ValidGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Range;

public record PostCartItemDTO(
        @NotNull(message = "产品id不能为空")
        Integer productId,
        @NotNull(groups = ValidGroup.AddCartItem.class)  // 分组校验
        @Range(min = 1, message = "商品数量不能小于1", groups = ValidGroup.AddCartItem.class)
        Integer quantity,
        Boolean selected
){ }
