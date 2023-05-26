package com.csu.mypetstore.api.domain.dto;

import com.csu.mypetstore.api.util.ValidGroup;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record PostOrderDTO(
        @NotNull(message = "地址不能为空")
        Integer addressId,

        Integer productId,

        Integer quantity
) {}
