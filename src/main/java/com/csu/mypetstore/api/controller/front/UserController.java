package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.User;

import com.csu.mypetstore.api.domain.structMapper.UserStructMapper;
import com.csu.mypetstore.api.domain.dto.LoginUserDTO;
import com.csu.mypetstore.api.domain.dto.RegisterUserDTO;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;
import com.csu.mypetstore.api.service.UserService;
import com.csu.mypetstore.api.domain.vo.ForgetQuestionVO;
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
    // 当一个类只有一个有参构造器，且该构造器不一定需要是public修饰的， 组件注入的时候不需要指定在构造器方法上或者构造器参数上指定@Autowired，只需要声明构造器即可；
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("api/session")
    public CommonResponse<UserInfoVO> login(@Valid @RequestBody LoginUserDTO loginUserDTO, HttpSession session){
        CommonResponse<UserInfoVO> result = userService.login(loginUserDTO.username(), loginUserDTO.password());
        if (result.isSuccess()){
            session.setAttribute(CONSTANT.LOGIN_USER, result.getData());
        }
        return result;
    }

    @DeleteMapping("api/session/{id}")
    public CommonResponse<String> logout(@PathVariable Integer id, HttpSession session) {  // 设计冗余属性id，之后的重构可能用到
        session.removeAttribute(CONSTANT.LOGIN_USER);
        return CommonResponse.createResponseForSuccess("退出登录成功");
    }

    @PostMapping("api/user")
    public CommonResponse<String> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        User user = UserStructMapper.INSTANCE.registerDTO2User(registerUserDTO);
        return userService.register(user, registerUserDTO.confirmPassword());
    }

    @GetMapping("api/fields/register")
    public CommonResponse<String> check_register_field(
            @RequestParam(value = "key") @NotBlank(message = "字段名不能为空") String fieldName,
            @RequestParam(value = "value") @NotBlank(message = "字段值不能为空") String fieldValue) {
        return userService.checkFieldDuplication(fieldName, fieldValue);
    }

    @PostMapping("api/getpwdquestion")
    public CommonResponse<ForgetQuestionVO> getForgetQuestion(@RequestBody Map<String, String> reqMap) {
        String username = reqMap.get("username");
        return userService.getForgetQuestion(username);
    }

    @GetMapping("api/user/{id}/pwdtoken")
    public CommonResponse<String> checkForgetAnswer(
            @PathVariable Integer id,
            @RequestParam @NotBlank(message = "密保问题不能为空") String question,
            @RequestParam @NotBlank(message = "回答不能为空") String answer){
        return userService.checkForgetAnswer(id, question, answer);
    }

    @PostMapping("api/user/{id}/password")
    public CommonResponse<String> resetPassword(@PathVariable Integer id, @RequestBody Map<String, String> reqMap, HttpSession session) {
        String oldPassword = reqMap.get("oldPassword");
        String newPassword = reqMap.get("newPassword");
        String token = reqMap.get("token");

        CommonResponse<String> result;
        if (!StringUtils.isEmpty(oldPassword))
            result =  userService.resetPassword(id, oldPassword, newPassword);
        result =  userService.resetForgetPassword(id, newPassword, token);

        if (result.isSuccess())  // 修改密码成功,需要重新登录
            session.removeAttribute(CONSTANT.LOGIN_USER);
        return result;
    }

    @GetMapping("api/user")
    public CommonResponse<UserInfoVO> getUserInfo(HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (user == null)
//            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());  // 前端会自动跳转
            return CommonResponse.createResponseForError("未登录用户不能查看用户信息");
        return CommonResponse.createResponseForSuccess(user);
    }

    @PatchMapping("api/user/{id}")
    public CommonResponse<String> updateUserInfo(
            @PathVariable Integer id,
            @RequestBody User updateUser,  //不加数据验证，字段均可为空
            HttpSession session) {
        UserInfoVO user = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (user == null || !id.equals(user.id()))
            return CommonResponse.createResponseForError(ResponseCode.NEED_LOGIN.getDescription(), ResponseCode.NEED_LOGIN.getCode());

        updateUser = updateUser.withId(id);
        return userService.updateUserInfo(updateUser);
    }
}
