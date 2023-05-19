package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.User;
import com.csu.mypetstore.api.domain.vo.ForgetQuestionVO;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;

public interface UserService {
    CommonResponse<UserInfoVO> login(String username, String password);

    CommonResponse<String> checkFieldDuplication(String fieldName, String fieldValue);

    CommonResponse<String> register(User user, String confirmPassword);

    CommonResponse<ForgetQuestionVO> getForgetQuestion(String username);

    CommonResponse<String> checkForgetAnswer(Integer id, String question,String answer);

    CommonResponse<String> resetForgetPassword(Integer id, String newPassword, String forgetToken);

    CommonResponse<UserInfoVO> getUserDetail(Integer userId);

    CommonResponse<String> resetPassword(Integer id, String oldPassword, String newPassword);

    CommonResponse<String> updateUserInfo(User user);
}
