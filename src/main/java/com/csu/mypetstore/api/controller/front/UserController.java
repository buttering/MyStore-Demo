package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.dto.dtoMapper.UserDTOMapper;
import com.csu.mypetstore.api.dto.LoginUserDTO;
import com.csu.mypetstore.api.dto.RegisterUserDTO;
import com.csu.mypetstore.api.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    public CommonResponse<User> login(@Valid @RequestBody LoginUserDTO loginUserDTO, HttpSession session){
        CommonResponse<User> result = userService.login(loginUserDTO.getUsername(), loginUserDTO.getPassword());
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
        User user = UserDTOMapper.INSTANCE.registerUserDTO2User(registerUserDTO);
        return userService.register(user, registerUserDTO.confirmPassword());
    }

    @GetMapping("api/fields/register")
    public CommonResponse<Object> check_register_field(
            @RequestParam(value = "key") @NotBlank(message = "字段名不能为空") String fieldName,
            @RequestParam(value = "value") @NotBlank(message = "字段值不能为空") String fieldValue) {
        return userService.checkFieldDuplication(fieldName, fieldValue);
    }

    @PostMapping("api/getpwdquestion")
    public CommonResponse<Object> getForgetQuestion(@RequestBody Map<String, String> reqMap) {
        String username = reqMap.get("username");
        return userService.getForgetQuestion(username);
    }

    @GetMapping("api/user/{id}/pwdtoken")
    public CommonResponse<String> checkForgetAnswer(@PathVariable Integer id, @RequestParam String question, @RequestParam String answer){
        return userService.checkForgetAnswer(id, question, answer);
    }

    @PostMapping("api/user/{id}/password")
    public CommonResponse<String> resetPassword(@PathVariable Integer id, @RequestBody Map<String, String> reqMap) {
        String oldPassword = reqMap.get("oldpassword");
        String newPassword = reqMap.get("newpassword");
        String token = reqMap.get("token");

        if (!StringUtils.isEmpty(oldPassword))
            return userService.resetPassword(id, oldPassword, newPassword);
        return userService.resetForgetPassword(id, newPassword, token);
    }
}
