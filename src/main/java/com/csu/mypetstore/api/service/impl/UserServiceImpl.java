package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.persistence.UserMapper;
import com.csu.mypetstore.api.service.UserService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Cache<String, String> localCache;

//    private final static HashMap<String, String> FIELD_MAP = new HashMap<>();

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, Cache<String, String> cache) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.localCache = cache;

//        FIELD_MAP.put("username", "用户名");
//        FIELD_MAP.put("phone", "电话号码");
//        FIELD_MAP.put("email", "电子邮件");
    }

    @Override
    public CommonResponse<User> login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            return CommonResponse.createResponseForError("用户名或密码错误");
        }

        boolean isPasswordMatch = passwordEncoder.matches(password, user.password());
        if (isPasswordMatch) {
            user = user.withPassword(StringUtils.EMPTY);  // 和字符串处理相关的操作（判等、判空、null等），使用工具类
            return CommonResponse.createResponseForSuccess(user);
        } else {
            return CommonResponse.createResponseForError("用户名或密码错误");
        }
    }

    // 检查字段是否重复
    @Override
    public CommonResponse<Object> checkFieldDuplication(String fieldName, String fieldValue) {
        String chineseFieldName = CONSTANT.REGISTER_FIELD_MAP.get(fieldName);
        if (chineseFieldName == null){
            return CommonResponse.createResponseForError(ResponseCode.ARGUMENT_ILLEGAL.getDescription(), ResponseCode.ARGUMENT_ILLEGAL.getCode());
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        long rows = userMapper.selectCount(queryWrapper.eq(fieldName, fieldValue));
        if (rows > 0){
            return CommonResponse.createResponseForError(String.format("%s已存在", chineseFieldName));
        }

        return CommonResponse.createResponseForSuccess();
    }

    @Override
    public CommonResponse<Object> register(User user) {
        CommonResponse<Object> checkResult = checkFieldDuplication("username", user.username());
        if (!checkResult.isSuccess()) return checkResult;
        checkResult = checkFieldDuplication("email", user.email());
        if (!checkResult.isSuccess()) return checkResult;
        checkResult = checkFieldDuplication("phone", user.phone());
        if (!checkResult.isSuccess()) return checkResult;

        // TODO: confirmPassword验证
        String password = passwordEncoder.encode(user.password());
        User newUser = user.withPassword(password)
                .withRole(CONSTANT.ROLE.CUSTOMER)
                .withCreateTime(LocalDateTime.now())
                .withUpdateTime(LocalDateTime.now());

        int rows = userMapper.insert(newUser);
        if (rows == 0) {
            return CommonResponse.createResponseForError("注册用户失败");
        }
        return CommonResponse.createResponseForSuccess("注册用户成功");
    }

    @Override
    public CommonResponse<String> getForgetQuestion(String username) {
        return null;
        // TODO: 用户名验证

    }

    @Override
    public CommonResponse<String> checkForgetAnswer(String username, String question, String answer) {
        // TODO: 验证答案
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("question", question).eq("answer", answer);

        long rows = userMapper.selectCount(queryWrapper);
        if (rows > 0){
            String forgetToken = username + UUID.randomUUID().toString();

//            logger.info("Put into LocalCache: ({}, {}), {}", username, forgetToken, LocalDateTime.now());
            localCache.put(username, forgetToken);

            return CommonResponse.createResponseForSuccess(forgetToken);
        }
        // TODO: 错误处理
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
