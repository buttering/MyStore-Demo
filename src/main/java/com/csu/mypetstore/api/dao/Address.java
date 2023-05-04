package com.csu.mypetstore.api.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "address")
public record Address(
        @TableId(value = "addid") int id,
        int uid, String address,
        @TableField(value = "phonenumber")
        int phone,
        @TableField(value = "`default`") boolean isDefault
) {}
