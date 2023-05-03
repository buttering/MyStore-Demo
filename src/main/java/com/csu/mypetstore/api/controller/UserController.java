package com.csu.mypetstore.api.controller;

import com.csu.mypetstore.api.dao.User;
import com.csu.mypetstore.api.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    @GetMapping("all")
    @ResponseBody
    public List<User> getAllUser(){
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        return userList;
    }
}
