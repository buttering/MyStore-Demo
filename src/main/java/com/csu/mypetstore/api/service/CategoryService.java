package com.csu.mypetstore.api.service;

import com.csu.mypetstore.api.common.CommonResponse;
import com.csu.mypetstore.api.domain.Category;
import com.csu.mypetstore.api.domain.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    // 仅自己
    public CommonResponse<CategoryVO> getCategoryById(Integer categoryId);

    // 直接子类，无递归
    public CommonResponse<List<CategoryVO>> getChildCategoryList(Integer categoryId);

    // 所有子类，递归查找
    public List<Category> getALLChildCategoryList(Integer categoryId);

    public CommonResponse<List<Integer>> getAllChildCategoryIdList(Integer categoryId);
}
