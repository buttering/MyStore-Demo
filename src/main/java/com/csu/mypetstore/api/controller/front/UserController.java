package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.dto.LoginUserDTO;
import com.csu.mypetstore.api.dto.RegisterUserDTO;
import com.csu.mypetstore.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.checkerframework.checker.units.qual.N;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    // 自动注入
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // DTO：数据传输对象，客户端向服务器提交数据的封装
    // VO：View对象/Value对象，后端向前端返回数据的封装/业务逻辑层和DAO层数据交换的封装
    // BO：领域（业务）对象，和实际业务一一对应
    @PostMapping("api/session")
    public CommonResponse<User> login(@Valid @RequestBody LoginUserDTO userDTO, HttpSession session){
        CommonResponse<User> result = userService.login(userDTO.getUsername(), userDTO.getPassword());
        if (result.isSuccess()){
            session.setAttribute(CONSTANT.LOGIN_USER, result.getData());
        }
        return result;
    }

    @DeleteMapping("api/session/{id}")
    public CommonResponse<User> logout(@PathVariable Integer id, HttpSession session) {  // 设计冗余属性id，之后的重构可能用到
        session.removeAttribute(CONSTANT.LOGIN_USER);
        return CommonResponse.createResponseForSuccess("退出登录成功");
    }

    @PostMapping("api/user")
    public CommonResponse<Object> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        return userService.register(dto2User(registerUserDTO));
    }

    private User dto2User(RegisterUserDTO registerUserDTO){

        // TODO: 便捷类型转换
        return new User(
                null,
                registerUserDTO.username(),
                registerUserDTO.password(),
                registerUserDTO.email(),
                registerUserDTO.phone(),
                registerUserDTO.question(),
                registerUserDTO.answer(),
                null,
                null,
                null
        );
    }


    public CommonResponse<Object> check_register_field(
            @RequestParam @NotBlank(message = "字段名不能为空") String fieldName,
            @RequestParam @NotBlank(message = "字段值不能为空") String fieldValue) {
        return userService.checkFieldDuplication(fieldName, fieldValue);
    }

    public CommonResponse<Object> getForgetQuestion() {
        // TODO: 获取忘记密码验证问题
        return null;
    }
}
