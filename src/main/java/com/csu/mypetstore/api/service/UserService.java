package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.User;

public interface UserService {
    CommonResponse<User> login(String username, String password);

    CommonResponse<Object> checkFieldDuplication(String fieldName, String fieldValue);

    CommonResponse<Object> register(User user, String confirmPassword);

    CommonResponse<Object> getForgetQuestion(String username);

    CommonResponse<String> checkForgetAnswer(Integer id, String question,String answer);

    CommonResponse<String> resetForgetPassword(Integer id, String newPassword, String forgetToken);

    CommonResponse<User> getUserDetail(Integer userId);

    CommonResponse<String> resetPassword(Integer id, String oldPassword, String newPassword);

    CommonResponse<Object> updateUserInfo(User user);
}
