package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName(value = "user")
public class User {
    @TableId(value = "uid", type = IdType.AUTO)  // auto:自增
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String email;
    private String phone;
    private String question;
    private String answer;
    @NotBlank(message = "角色不能为空")
    private Integer role;

    @TableField(value = "create_time")
    @NotBlank(message = "创建时间不能为空")
    private LocalDateTime createTime;
    @TableField(value = "update_time")
    @NotBlank(message = "更新时间不能为空")
    private LocalDateTime updateTime;
}
