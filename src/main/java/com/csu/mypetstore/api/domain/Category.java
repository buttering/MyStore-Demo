package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;

public record Category(
        @TableId(type = IdType.AUTO)
        Integer id,
        @TableField(value = "parent_id")
        Integer parentId,

        @NotBlank(message = "商品类别名不能为空")
        String name,
        Integer status,
        Integer sortOrder,

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    // 如果对象的equals方法被重写，那么对象的HashCode方法也尽量重写
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
