package com.csu.mypetstore.api.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.With;

import java.time.LocalDateTime;


// 会话跟踪 session cookie token

@With
@TableName(value = "user")
public record User(
        @TableId(type = IdType.AUTO)  // auto:自增，该类型请确保数据库设置了 ID自增 否则无效
        Integer id,
        @NotBlank(message = "用户名不能为空")
        String username,
        @NotBlank(message = "密码不能为空")
        String password,
        String email,
        String phone,
        @NotBlank(message = "密保问题不能为空")
        String question,
        @NotBlank(message = "密保回答不能为空")
        String answer,
        Integer role,

        @TableField(value = "create_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createTime,
        @TableField(value = "update_time")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime updateTime
) {
        public User setId(Integer id){
                return new User(id, username, password, email, phone, question, answer, role, createTime, updateTime);
        }
}
