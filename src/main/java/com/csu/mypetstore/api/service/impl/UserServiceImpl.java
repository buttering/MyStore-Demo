package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.persistence.UserMapper;
import com.csu.mypetstore.api.service.UserService;
import com.csu.mypetstore.api.vo.ForgetQuestionVO;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Override
    public CommonResponse<Object> register(User user, String confirmPassword) {
        CommonResponse<Object> checkResult = checkFieldDuplication("username", user.username());
        if (!checkResult.isSuccess()) return checkResult;
        checkResult = checkFieldDuplication("email", user.email());
        if (!checkResult.isSuccess()) return checkResult;
        checkResult = checkFieldDuplication("phone", user.phone());
        if (!checkResult.isSuccess()) return checkResult;
        checkResult = checkPassword(user.password(), confirmPassword);
        if (!checkResult.isSuccess()) return checkResult;

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

    // 检查字段是否重复，不重复则返回成功
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

    // 检查密码格式是否符合要求
    private CommonResponse<Object> checkPassword(String password, String confirmPassword) {
        int minimumLength = 8;

        if ( ! StringUtils.equals(password, confirmPassword))
            return CommonResponse.createResponseForError("两次输入密码不一致");
        if (password.length() < minimumLength)
            return CommonResponse.createResponseForError("密码长度不足");
        return CommonResponse.createResponseForSuccess();
    }

    @Override
    public CommonResponse<Object> getForgetQuestion(String username) {
        CommonResponse<Object> checkResult = checkFieldDuplication("username", username);
        if (checkResult.isSuccess())
            return CommonResponse.createResponseForError("用户不存在");

        QueryWrapper<User> queryWrapper = Wrappers.<User>query().eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (StringUtils.isBlank(user.question()))
            return CommonResponse.createResponseForError("未设置密保问题，如需找回密码请联系管理员");
        return CommonResponse.createResponseForSuccess(new ForgetQuestionVO(user.question(), user.id()));
    }

    @Override
    public CommonResponse<String> checkForgetAnswer(Integer id, String question, String answer) {
        // TODO: 验证答案
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("question", question).eq("answer", answer);

        long rows = userMapper.selectCount(queryWrapper);
        if (rows > 0){
            String forgetToken = id + UUID.randomUUID().toString();

            log.info("Put into LocalCache: ({}, {}), {}", id, forgetToken, LocalDateTime.now());
            localCache.put(String.valueOf(id), forgetToken);

            return CommonResponse.createResponseForSuccess(forgetToken);
        }
        return CommonResponse.createResponseForError("密保回答错误");
    }

    @Override
    public CommonResponse<String> resetForgetPassword(Integer id, String newPassword, String forgetToken) {
        return null;
    }

    @Override
    public CommonResponse<User> getUserDetail(Integer userId) {
        return null;
    }

    @Override
    public CommonResponse<String> resetPassword(Integer id, String oldPassword, String newPassword) {
        // TODO: 完成逻辑
        return null;
    }

    @Override
    public CommonResponse<Object> updateUserInfo(User user) {
        return null;
    }
}
