package com.csu.mypetstore.api.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterUserDTO (
    @NotBlank(message = "用户名不能为空")
    String username,
    @NotBlank(message = "密码不能为空")
    String password,
    @NotBlank(message = "重复密码不能为空")
    String confirmPassword,
    String email,
    String phone,
    @NotBlank(message = "密保不能为空")
    String question,
    @NotBlank(message = "密保答案不能为空")
    String answer
) { }
