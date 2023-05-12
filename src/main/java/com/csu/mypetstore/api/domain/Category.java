package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record Category(
        @TableId(type = IdType.AUTO)
        Integer id,
        @TableField(value = "parent_id")
        Integer parentId,

        @NotBlank(message = "商品类别名不能为空")
        String name,
        Boolean status,
        String sortOrder,

        @TableField(value = "create_time")
        @NotBlank(message = "创建时间不能为空")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @NotBlank(message = "更新时间不能为空")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updateTime
) {
    @Override
    public boolean equals(Object obj) {
        return true;
        // TODO: 比较id
    }

    @Override
    public int hashCode() {
        return 0;
        // TODO:  比较id
    }
}
