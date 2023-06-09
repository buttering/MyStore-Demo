package com.csu.mypetstore.api.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


// DTO：数据传输对象，客户端向服务器提交数据的封装，以及各个层之间传递数据
// VO：View对象/Value对象，后端向前端返回数据的封装/业务逻辑层和DAO层数据交换的封装
// BO：领域（业务）对象，和实际业务一一对应
public record LoginUserDTO (
        @NotBlank(message = "姓名不能为空")
        String username,
        @NotBlank(message = "密码不能为空")
        String password
) {}