package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

public record Cart (
    @TableId(type = IdType.AUTO)
    Integer id,
    @NotBlank(message = "用户id不能为空")
    Integer uid,
    @TableField(value = "product_id")
    @NotBlank(message = "产品id不能为空")
    Integer productId,

    @NotBlank(message = "数量不能为空")
    Integer quantity,

    @TableField(value = "create_time")
    @NotBlank(message = "创建时间不能为空")
    LocalDateTime createTime,
    @TableField(value = "update_time")
    @NotBlank(message = "更新时间不能为空")
    LocalDateTime updateTime
) { }
