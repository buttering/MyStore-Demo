package com.csu.mypetstore.api.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.csu.mypetstore.api.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UserInfoVO(
        Integer id,
        String username,
        String email,
        String phone,
        Integer role
){
}
