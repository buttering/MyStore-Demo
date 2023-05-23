package com.csu.mypetstore.api.controller.admin;

import com.csu.mypetstore.api.common.CONSTANT;
import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.domain.vo.CategoryVO;
import com.csu.mypetstore.api.domain.vo.UserInfoVO;
import com.csu.mypetstore.api.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // TODO: 完成获取类别的接口
    @GetMapping("admin/categories/{id}")
    public CommonResponse<CategoryVO> getCategoryById (@PathVariable(value = "id") Integer categoryId, HttpSession session) {
        UserInfoVO userInfoVO = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (userInfoVO == null || userInfoVO.role() != CONSTANT.ROLE.ADMIN)
            return CommonResponse.createResponseForError("未登录或非管理员账号，授权失败");

        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping("admin/categories/{id}/children")
    public CommonResponse<List<CategoryVO>> getChildrenCategoryListById(@PathVariable(value = "id") Integer categoryId, HttpSession session) {
        UserInfoVO userInfoVO = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (userInfoVO == null || userInfoVO.role() != CONSTANT.ROLE.ADMIN)
            return CommonResponse.createResponseForError("未登录或非管理员账号，授权失败");

        return categoryService.getChildCategoryList(categoryId);
    }

    @GetMapping("admin/categories/{id}/children/id")
    public CommonResponse<List<Integer>> getChildrenCategoryIdListById(@PathVariable(value = "id") Integer categoryId, HttpSession session) {
        UserInfoVO userInfoVO = (UserInfoVO) session.getAttribute(CONSTANT.LOGIN_USER);
        if (userInfoVO == null || userInfoVO.role() != CONSTANT.ROLE.ADMIN)
            return CommonResponse.createResponseForError("未登录或非管理员账号，授权失败");

        return categoryService.getAllChildCategoryIdList(categoryId);
    }
}
;