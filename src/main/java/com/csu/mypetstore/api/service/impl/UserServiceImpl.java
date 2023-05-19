package com.csu.mypetstore.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.common.ResponseCode;
import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.domain.structMapper.UserStructMapper;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;
import com.csu.mypetstore.api.persistence.UserMapper;
import com.csu.mypetstore.api.service.UserService;
import com.csu.mypetstore.api.domain.vo.ForgetQuestionVO;
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

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, Cache<String, String> cache) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.localCache = cache;
    }

    @Override
    public CommonResponse<UserInfoVO> login(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null){
            return CommonResponse.createResponseForError("用户名或密码错误");
        }

        boolean isPasswordMatch = passwordEncoder.matches(password, user.password());
        if (isPasswordMatch) {
            UserInfoVO userInfoVO = UserStructMapper.INSTANCE.user2InfoVO(user);
//            user = user.withPassword(StringUtils.EMPTY).withAnswer(StringUtils.EMPTY).withAnswer(StringUtils.EMPTY);
            return CommonResponse.createResponseForSuccess(userInfoVO);
        } else {
            return CommonResponse.createResponseForError("用户名或密码错误");
        }
    }

    @Override
    public CommonResponse<String> register(User user, String confirmPassword) {
        CommonResponse<String> checkResult = checkFieldDuplication("username", user.username());
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
    public CommonResponse<String> checkFieldDuplication(String fieldName, String fieldValue) {
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
    private CommonResponse<String> checkPassword(String password, String confirmPassword) {
        if ( ! StringUtils.equals(password, confirmPassword))
            return CommonResponse.createResponseForError("两次输入密码不一致");
        return checkPassword(password);
    }

    private CommonResponse<String> checkPassword(String password) {
        int minimumLength = 8;
        if (password.length() < minimumLength)
            return CommonResponse.createResponseForError("密码长度不足");
        if (StringUtils.countMatches(password, ' ') > 0)
            return CommonResponse.createResponseForError("密码不能包含空格");
        return CommonResponse.createResponseForSuccess();
    }

    @Override
    public CommonResponse<ForgetQuestionVO> getForgetQuestion(String username) {
        CommonResponse<String> checkResult = checkFieldDuplication("username", username);
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
    public CommonResponse<String> resetPassword(Integer id, String oldPassword, String newPassword) {
        User user = userMapper.selectOne(Wrappers.<User>query().eq("id", id));
        if (user == null)
            return CommonResponse.createResponseForError("用户不存在");
        if (passwordEncoder.matches(oldPassword, user.password()))
            return CommonResponse.createResponseForError("旧密码输入错误");
        CommonResponse<String> checkResult = checkPassword(newPassword);
        if (!checkResult.isSuccess())
            return checkResult;

        return updatePassword(user, newPassword);
    }

    @Override
    public CommonResponse<String> resetForgetPassword(Integer id, String newPassword, String forgetToken) {
        User user = userMapper.selectOne(Wrappers.<User>query().eq("id", id));
        if (user == null)
            return CommonResponse.createResponseForError("用户不存在");
        String cacheToken = localCache.getIfPresent(String.valueOf(id));
        log.info("Get token from LocalCache : ({},{}) {}" ,id, cacheToken, LocalDateTime.now());
        if (StringUtils.isEmpty(cacheToken) || !StringUtils.equals(cacheToken, forgetToken))
            return CommonResponse.createResponseForError("token无效或已过期");

        return updatePassword(user, newPassword);
    }

    // 已经通过校验，修改密码
    private CommonResponse<String> updatePassword(User user, String password){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.id())
                .set("password", passwordEncoder.encode(password))
                .set("update_time", LocalDateTime.now());
        long row = userMapper.update(null, updateWrapper);

        if (row > 0)
            return CommonResponse.createResponseForSuccess();
        log.error("id:{}用户密码更新失败, {}", user.id(), LocalDateTime.now());
        return CommonResponse.createResponseForError("密码更新失败");
    }

    @Override
    public CommonResponse<UserInfoVO> getUserDetail(Integer userId) {
        return null;
    }

    @Override
    public CommonResponse<String> updateUserInfo(User user) {
        CommonResponse<String> checkResult = checkFieldDuplication("username", user.username());
        if (!checkResult.isSuccess()) return checkResult;
        checkResult = checkFieldDuplication("email", user.email());
        if (!checkResult.isSuccess()) return checkResult;
        checkResult = checkFieldDuplication("phone", user.phone());
        if (!checkResult.isSuccess()) return checkResult;

        boolean isAllNull = true;
        UpdateWrapper <User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", user.id());
        if (!StringUtils.isBlank(user.username())) {
            updateWrapper.set("username", user.username());
            isAllNull = false;
        }
        if (!StringUtils.isBlank(user.email())) {
            updateWrapper.set("email", user.email());
            isAllNull = false;
        }
        if (!StringUtils.isBlank(user.phone())) {
            updateWrapper.set("phone", user.phone());
            isAllNull = false;
        }
        if (!StringUtils.isBlank(user.question())) {
            if (StringUtils.isBlank(user.answer()))
                return CommonResponse.createResponseForError("密保未设置对应回答");
            else {
                updateWrapper.set("question", user.question());
                updateWrapper.set("answer", user.answer());
                isAllNull = false;
            }
        }

        if (!isAllNull) {
            updateWrapper.set("update_time", LocalDateTime.now());
            long row = userMapper.update(null, updateWrapper);
            if (row > 0)
                return CommonResponse.createResponseForSuccess();
        }
        return CommonResponse.createResponseForError("用户信息更新失败");
    }
}
