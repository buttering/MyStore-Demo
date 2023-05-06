package com.csu.mypetstore.api.controller.front;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.dto.LoginUserDTO;
import com.csu.mypetstore.api.persistence.UserMapper;
import com.csu.mypetstore.api.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // DTO：数据传输对象，客户端向服务器提交数据的封装
    // VO：View对象/Value对象，后端向前端返回数据的封装/业务逻辑层和DAO层数据交换的封装
    // BO：领域（业务）对象，和实际业务一一对应
    @PostMapping("login")
    public CommonResponse<User> login(@Valid @RequestBody LoginUserDTO userDTO, HttpSession session){
        CommonResponse<User> result = userService.login(userDTO.getUsername(), userDTO.getPassword());
        if (result.isSuccess()){
            // TODO:session
            session.setAttribute("login user", result.getData());
        }
        return result;
    }
}
