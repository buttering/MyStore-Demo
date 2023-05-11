package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.User;

public interface UserService {
    CommonResponse<User> login(String username, String password);

    CommonResponse<Object> checkFieldDuplication(String fieldName, String fieldValue);

    CommonResponse<Object> register(User user);

    CommonResponse<String> getForgetQuestion(String username);

    CommonResponse<String> checkForgetAnswer(String username, String question,String answer);

    CommonResponse<Object> resetForgetPassword(String username, String newPassword, String forgetToken);

    CommonResponse<User> getUserDetail(Integer userId);

    CommonResponse<Object> resetPassword(String oldPassword, String newPassword, User user);

    CommonResponse<Object> updateUserInfo(User user);
}
