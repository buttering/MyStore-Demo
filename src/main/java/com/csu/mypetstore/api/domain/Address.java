package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@TableName(value = "address")
public record Address(
        @TableId(type = IdType.AUTO)
        Integer id,  // 基本类型有默认值，可能会有bug。包装类默认为null
        @NotBlank(message = "用户id不能为空")
        Integer uid,

        @NotBlank(message = "地址名称不能为空")
        @TableField(value = "address_name")
        String addressName,
        String phone,
        @NotBlank(message = "移动电话不能为空")
        String mobile,
        @NotBlank(message = "省份不能为空")
        String province,
        @NotBlank(message = "城市不能为空")
        String city,
        @NotBlank(message = "区县不能为空")
        String district,
        @NotBlank(message = "详细地址不能为空")
        String detail,
        String zip,

        @TableField(value = "create_time")
        @NotBlank(message = "创建时间不能为空")
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @NotBlank(message = "更新时间不能为空")
        LocalDateTime updateTime
) {}
