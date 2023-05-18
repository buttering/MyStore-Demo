package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.With;

import java.time.LocalDateTime;

@Data
@TableName("productimage")
public class ProductImage {
        @TableId(type = IdType.ASSIGN_UUID)  // 使用UUID时，record类型会导致错误且无法向数据库增加记录
        private final String id;  // 图片索引，使用uuid
        private final Integer pid;  // 产品id
        private final Integer type;  // 图片类型
        private final String alt; // 图片注释

        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        private final LocalDateTime createTime;
}
