package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.persistence.UserMapper;
import com.csu.mypetstore.api.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public CommonResponse<User> login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", password);

        // TODO:加解密

        User user = userMapper.selectOne(queryWrapper);

        if (user == null){
            return CommonResponse.createResponseForError("用户名或密码错误");
        } else {
            return CommonResponse.createResponseForSuccess(user);
        }
    }

    @Override
    public CommonResponse<Object> checkField(String fieldName, String fieldValue) {
        return null;
    }

    @Override
    public CommonResponse<Object> register(User user) {
        return null;
    }

    @Override
    public CommonResponse<String> getForgetQuestion(String username) {
        return null;
    }

    @Override
    public CommonResponse<String> checkForgetAnswer(String username, String question, String answer) {
        return null;
    }

    @Override
    public CommonResponse<Object> resetForgetPassword(String username, String newPassword, String forgetToken) {
        return null;
    }

    @Override
    public CommonResponse<User> getUserDetail(Integer userId) {
        return null;
    }

    @Override
    public CommonResponse<Object> resetPassword(String oldPassword, String newPassword, User user) {
        return null;
    }

    @Override
    public CommonResponse<Object> updateUserInfo(User user) {
        return null;
    }
}
